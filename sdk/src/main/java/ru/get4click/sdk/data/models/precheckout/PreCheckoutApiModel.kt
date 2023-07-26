package ru.get4click.sdk.data.models.precheckout

internal data class PreCheckoutApiModel(
    val widgetId: Int,
    val base_colour: String,
    val messages: List<PreCheckoutItemData>
)
