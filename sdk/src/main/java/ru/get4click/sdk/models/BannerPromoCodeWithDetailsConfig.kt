package ru.get4click.sdk.models

/**
 * Full configuration for banner view
 *
 * @param title the title of the banner, usually it is a company name
 * @param discountText the size of a discount (example: 10%)
 * @param descriptionMain description for the given promo code
 * @param descriptionDetailed description for the given promo code when user clicks "More"
 * @param staticConfig static data configuration
 */
data class BannerPromoCodeWithDetailsConfig(
    val title: String,
    val discountText: String,
    val descriptionMain: List<String>,
    val descriptionDetailed: List<String>,
    val promoCode: String,
    val staticConfig: BannerPromoCodeWithDetailsStaticConfig
)
