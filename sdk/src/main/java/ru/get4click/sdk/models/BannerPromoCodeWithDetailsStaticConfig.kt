package ru.get4click.sdk.models

import androidx.annotation.ColorInt

/**
 * Configure promo code banner view but only for data that is predefined (for static data)
 *
 * @param promoCodeTitle the second title right under the promo code display area
 * @param primaryButtonColor the color of the main button "Copy"
 * @param primaryButtonTextColor the color of the text of the main button "Copy"
 * @param backgroundColor background color
 * @param textColor text color
 */
class BannerPromoCodeWithDetailsStaticConfig(
    val promoCodeTitle: String? = null,
    @ColorInt val primaryButtonColor: Int? = null,
    @ColorInt val primaryButtonTextColor: Int? = null,
    @ColorInt val backgroundColor: Int? = null,
    @ColorInt val textColor: Int? = null
)