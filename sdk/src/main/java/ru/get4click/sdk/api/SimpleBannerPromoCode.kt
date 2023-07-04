package ru.get4click.sdk.api

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.get4click.sdk.data.BannerPromoCodeApi
import ru.get4click.sdk.models.BannerPromoCodeConfig
import ru.get4click.sdk.models.BannerPromoCodeStaticConfig
import ru.get4click.sdk.ui.bannerpromocode.BannerPromoCodeDialog
import ru.get4click.sdk.ui.bannerpromocode.BannerPromoCodeWithDetailsDialog
import ru.get4click.sdk.ui.bannerpromocode.SimpleBannerPromoCodeDialog
import ru.get4click.sdk.ui.bannerpromocode.PromoCodeCreds

internal class SimpleBannerPromoCode(
    private val activity: ComponentActivity,
    private val promoCodeCreds: PromoCodeCreds,
    private val config: BannerPromoCodeStaticConfig,
    private val bannerPromoCodeApi: BannerPromoCodeApi
) : BannerPromoCode {
    private var bannerDialog: BannerPromoCodeDialog? = null

    private var bannerFullConfig: BannerPromoCodeConfig? = null

    private val scope = activity.lifecycleScope

    init {
        scope.launch(Dispatchers.IO) {
            bannerPromoCodeApi.getPromoCodeData(promoCodeCreds)
                .onSuccess { promoCodeModel ->
                    bannerFullConfig = BannerPromoCodeConfig(
                        title        = "",
                        discountText = promoCodeModel.couponTitle,
                        description  = promoCodeModel.couponDescription,
                        limitations  = promoCodeModel.couponLimitations,
                        restrictions = promoCodeModel.orderRestrictions,
                        promoCode    = promoCodeModel.couponCode,
                        staticConfig = config
                    )
                }.onFailure {
                    Log.e(TAG, it.message ?: "")
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

    companion object {
        private const val TAG = "Promo Code"
    }
}