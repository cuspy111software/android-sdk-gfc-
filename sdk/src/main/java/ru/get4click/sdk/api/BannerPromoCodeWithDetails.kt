//package ru.get4click.sdk.api
//
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.lifecycle.lifecycleScope
//import kotlinx.coroutines.launch
//import ru.get4click.sdk.data.BannerPromoCodeApi
//import ru.get4click.sdk.data.models.Email
//import ru.get4click.sdk.models.BannerPromoCodeWithDetailsConfig
//import ru.get4click.sdk.models.BannerPromoCodeWithDetailsStaticConfig
//import ru.get4click.sdk.ui.bannerpromocode.BannerPromoCodeWithDetailsDialog
//import ru.get4click.sdk.ui.bannerpromocode.PromoCodeCreds
//
//internal class BannerPromoCodeWithDetails(
//    private val activity: ComponentActivity,
//    private val promoCodeCreds: PromoCodeCreds,
//    private val config: BannerPromoCodeWithDetailsStaticConfig,
//    private val bannerPromoCodeApi: BannerPromoCodeApi
//) : BannerPromoCode {
//    private var bannerDialog: BannerPromoCodeWithDetailsDialog? = null
//
//    private var bannerFullConfig: BannerPromoCodeWithDetailsConfig? = null
//
//    private val scope = activity.lifecycleScope
//
//    init {
//        scope.launch {
//            bannerPromoCodeApi.getPromoCodeData(
//                apiKey = promoCodeCreds.apiKey,
//                email  = Email(promoCodeCreds.email)
//            ).onSuccess { promoCodeModel ->
//                bannerFullConfig = BannerPromoCodeWithDetailsConfig(
//                    title               = promoCodeModel.couponDescription,
//                    discountText        = promoCodeModel.couponTitle,
//                    descriptionMain     = listOf(promoCodeModel.couponLimitations),
//                    descriptionDetailed = listOf(),
//                    promoCode           = promoCodeModel.couponCode,
//                    staticConfig        = config
//                )
//            }.onFailure {
//                Log.i("SimpleBannerPromoCode", it.message ?: it.stackTraceToString())
//            }
//        }
//    }
//
//    override fun show() {
//        if (bannerDialog == null) {
//            bannerFullConfig?.let {
//                bannerDialog = BannerPromoCodeWithDetailsDialog(activity, it)
//            }
//        }
//
//        bannerDialog?.show()
//    }
//}
