package ru.get4click.sdk.models

/**
 * Full configuration for banner view
 *
 * @param title the title of the banner, usually it is a company name
 * @param discountText the size of a discount (example: 10%)
 * @param description description for the given promo code
 * @param staticConfig static data configuration
 */
internal data class BannerPromoCodeConfig(
    val title: String,
    val discountText: String,
    val description: String,
    val limitations: String? = null,
    val restrictions: String? = null,
    val logo: String? = null,
    val promoCode: String,
    val distributionId: Int,
    val staticConfig: BannerPromoCodeStaticConfig
)
