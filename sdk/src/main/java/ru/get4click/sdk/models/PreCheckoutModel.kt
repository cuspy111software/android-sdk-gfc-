package ru.get4click.sdk.models

import ru.get4click.sdk.data.models.precheckout.PreCheckoutItemData

internal data class PreCheckoutModel(
    val hiding_time: Long,
    val widgetId: Int,
    val baseColor: String,
    val messages: List<PreCheckoutItemData>,
    val sessionId: String,
)
