package ru.get4click.sdk.api

import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.get4click.sdk.R
import ru.get4click.sdk.data.BannerPromoCodeApi
import ru.get4click.sdk.models.BannerPromoCodeConfig
import ru.get4click.sdk.models.BannerPromoCodeStaticConfig
import ru.get4click.sdk.ui.bannerpromocode.BannerPromoCodeDialog
import ru.get4click.sdk.ui.bannerpromocode.BannerPromoCodeWithDetailsDialog
import ru.get4click.sdk.ui.bannerpromocode.PromoCodeCreds
import ru.get4click.sdk.ui.bannerpromocode.SimpleBannerPromoCodeDialog
import ru.get4click.sdk.ui.wheeloffortune.BannerPromoCodeInteractor

internal class SimpleBannerPromoCode(
    private val activity: ComponentActivity,
    private val promoCodeCreds: PromoCodeCreds,
    private val viewType: BannerPromoCodeViewType,
    private val config: BannerPromoCodeStaticConfig,
    private val bannerPromoCodeApi: BannerPromoCodeApi,
    private val promoCodeListener: BannerPromoCodeListener
) : BannerPromoCode, BannerPromoCodeInteractor {
    private var bannerDialog: BannerPromoCodeDialog? = null

    private var bannerFullConfig: BannerPromoCodeConfig? = null

    private var buttonPromoCode: ImageView? = null

    private var alreadyApplied = false

    private val scope = activity.lifecycleScope

    init {
        scope.launch(Dispatchers.IO) {
            bannerPromoCodeApi.getPromoCodeData(promoCodeCreds)
                .onSuccess { promoCodeModel ->
                    bannerFullConfig = BannerPromoCodeConfig(
                        title          = "",
                        discountText   = promoCodeModel.couponTitle,
                        description    = promoCodeModel.couponDescription,
                        limitations    = promoCodeModel.couponLimitations.ifEmpty { null },
                        restrictions   = promoCodeModel.orderRestrictions.ifEmpty { null },
                        promoCode      = promoCodeModel.couponCode,
                        distributionId = promoCodeModel.distributionId,
                        logo           = promoCodeModel.logoUrl.ifEmpty { null },
                        staticConfig   = config
                    )
                    withContext(Dispatchers.Main) {
                        promoCodeListener.onInit(this@SimpleBannerPromoCode)
                    }
                }.onFailure { e ->
                    Log.e(TAG, e.message ?: "")
                    withContext(Dispatchers.Main) {
                        promoCodeListener.onInitFailed(e)
                    }
                }
        }
    }

    override fun show() {
        if (alreadyApplied) return

        if (bannerDialog == null) {
            bannerFullConfig?.let { config ->
                bannerDialog = when (viewType) {
                    BannerPromoCodeViewType.Simple ->
                        SimpleBannerPromoCodeDialog(
                            context                = activity,
                            bannerDialogInteractor = this,
                            config                 = config
                        )
                    BannerPromoCodeViewType.Expandable ->
                        BannerPromoCodeWithDetailsDialog(
                            context                = activity,
                            bannerDialogInteractor = this,
                            config                 = config
                        )
                }
            }
        }

        bannerDialog?.show()
    }

    override fun getButton(): ImageView? {
        if (alreadyApplied || bannerFullConfig == null) return null

        return buttonPromoCode ?: ImageView(activity)
            .apply {
                val buttonSize = activity.resources
                    .getDimension(R.dimen.default_image_button_size).toInt()

                layoutParams = ViewGroup.LayoutParams(buttonSize, buttonSize)
                setImageResource(R.drawable.ic_promo_code_button)

                setOnClickListener { show() }
            }.also { buttonPromoCode = it }
    }

    override fun promoCodeAlreadyUsed() {
        val distributionId = bannerFullConfig?.distributionId ?: return

        bannerDialog?.dismiss()
        buttonPromoCode?.isVisible = false
        scope.launch(Dispatchers.IO) {
            bannerPromoCodeApi.promoCodeIsAlreadyUsed(promoCodeCreds, distributionId)
                .onFailure { Log.e(TAG, "Failed to send \"Promo Code is already used\"") }
        }
    }

    companion object {
        private const val TAG = "Promo Code"
    }
}
