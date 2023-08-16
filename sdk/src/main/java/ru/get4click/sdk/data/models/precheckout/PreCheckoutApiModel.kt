package ru.get4click.sdk.data.models.precheckout

internal data class PreCheckoutApiModel(
    val hiding_time: Long,
    val widgetId: Int,
    val base_colour: String,
    val messages: List<PreCheckoutItemData>,
    val session_id: String
)
