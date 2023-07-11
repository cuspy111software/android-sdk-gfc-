package ru.get4click.sdk.data.models.promocode

internal data class PromoCodeApiModel(
    val couponTitle: String,
    val couponDescription: String,
    val couponLimitations: String,
    val orderRestrictions: String,
    val couponCode: String,
    val distributionId: Int,
    val logoUrl: String
)
