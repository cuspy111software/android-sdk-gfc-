package ru.get4click.sdk.models

import androidx.annotation.ColorInt

/**
 * Configure promo code banner view but only for data that is predefined (for static data)
 *
 * @param promoCodeTitle the second title right under the promo code display area
 * @param primaryButtonColor the color of the main button "Copy"
 * @param primaryButtonTextColor the color of the text of the main button "Copy"
 * @param bottomPartColor the color of the bottom part of the banner
 * @param topPartColor the color of the top part of the banner
 * @param bottomPartTextColor the color of the text in the bottom part of the banner
 * @param topPartTextColor the color of the text in the top part of the banner
 */
data class BannerPromoCodeStaticConfig(
    val promoCodeTitle: String? = null,
    @ColorInt val primaryButtonColor: Int? = null,
    @ColorInt val primaryButtonTextColor: Int? = null,
    @ColorInt val bottomPartColor: Int? = null,
    @ColorInt val topPartColor: Int? = null,
    @ColorInt val bottomPartTextColor: Int? = null,
    @ColorInt val topPartTextColor: Int? = null
)
