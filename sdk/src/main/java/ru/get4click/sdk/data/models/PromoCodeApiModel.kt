package ru.get4click.sdk.data.models

data class PromoCodeApiModel(
    val couponTitle: String,
    val couponDescription: String,
    val couponLimitations: String,
    val orderRestrictions: String,
    val couponCode: String
)
