package ru.get4click.sdk.api

import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.ComponentActivity
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

internal class SimpleBannerPromoCode(
    private val activity: ComponentActivity,
    private val promoCodeCreds: PromoCodeCreds,
    private val config: BannerPromoCodeStaticConfig,
    private val bannerPromoCodeApi: BannerPromoCodeApi,
    private val promoCodeListener: BannerPromoCodeListener
) : BannerPromoCode {
    private var bannerDialog: BannerPromoCodeDialog? = null

    private var bannerFullConfig: BannerPromoCodeConfig? = null

    private var buttonPromoCode: ImageView? = null

    private val scope = activity.lifecycleScope

    init {
        scope.launch(Dispatchers.IO) {
            bannerPromoCodeApi.getPromoCodeData(promoCodeCreds)
                .onSuccess { promoCodeModel ->
                    bannerFullConfig = BannerPromoCodeConfig(
                        title        = "",
                        discountText = promoCodeModel.couponTitle,
                        description  = promoCodeModel.couponDescription,
                        limitations  = promoCodeModel.couponLimitations.ifEmpty { null },
                        restrictions = promoCodeModel.orderRestrictions.ifEmpty { null },
                        promoCode    = promoCodeModel.couponCode,
                        logo         = promoCodeModel.logoUrl.ifEmpty { null },
                        staticConfig = config
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
        if (bannerDialog == null) {
            bannerFullConfig?.let { config ->
                bannerDialog = if (config.limitations == null && config.restrictions == null) {
                    SimpleBannerPromoCodeDialog(activity, config)
                } else {
                    BannerPromoCodeWithDetailsDialog(activity, config)
                }
            }
        }

        bannerDialog?.show()
    }

    override fun getButton(): ImageView {
        return buttonPromoCode ?: ImageView(activity)
            .apply {
                val buttonSize = activity.resources
                    .getDimension(R.dimen.default_image_button_size).toInt()

                layoutParams = ViewGroup.LayoutParams(buttonSize, buttonSize)
                setImageResource(R.drawable.ic_promo_code_button)

                setOnClickListener { show() }
            }.also { buttonPromoCode = it }
    }

    companion object {
        private const val TAG = "Promo Code"
    }
}
