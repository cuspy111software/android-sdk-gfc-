package ru.get4click.sdk.data

import ru.get4click.sdk.data.models.precheckout.PreCheckoutApiModel
import ru.get4click.sdk.data.models.precheckout.PreCheckoutCloseApiModel

internal interface PreCheckoutApi {
    suspend fun getPreCheckoutData(apiKey: String, shopId: Int): Result<PreCheckoutApiModel>
    suspend fun sendNotifyClose(apiKey: String, widgetId: Int, userAction: String): Result<PreCheckoutCloseApiModel>
}
