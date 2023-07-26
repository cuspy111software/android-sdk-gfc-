package ru.get4click.sdk.data

import ru.get4click.sdk.data.models.precheckout.PreCheckoutApiModel

internal interface PreCheckoutApi {
    suspend fun getPreCheckoutData(apiKey: String, shopId: Int): Result<PreCheckoutApiModel>
}
