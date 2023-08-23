package ru.get4click.sdk.data.models.precheckout

internal data class PreCheckoutApiModel(
    val hidingTime: Long,
    val widgetId: Int,
    val baseColour: String,
    val messages: List<PreCheckoutItemData>,
    val sessionId: String
)
