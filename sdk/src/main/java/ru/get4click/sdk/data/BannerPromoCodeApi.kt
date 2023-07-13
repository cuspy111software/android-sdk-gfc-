package ru.get4click.sdk.data

import ru.get4click.sdk.data.models.promocode.PromoCodeApiModel
import ru.get4click.sdk.ui.bannerpromocode.PromoCodeCreds

internal interface BannerPromoCodeApi {
    suspend fun getPromoCodeData(promoCodeCreds: PromoCodeCreds): Result<PromoCodeApiModel>

    suspend fun promoCodeIsAlreadyUsed(
        promoCodeCreds: PromoCodeCreds,
        couponCodeDistributionId: Int
    ): Result<Unit>
}
