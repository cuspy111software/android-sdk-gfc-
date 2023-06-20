package ru.get4click.sdk.data

import ru.get4click.sdk.data.models.Email
import ru.get4click.sdk.data.models.GiftId
import ru.get4click.sdk.data.models.wheel.WheelOfFortuneConfigData

internal interface WheelOfFortuneApi {
    suspend fun init(apiKey: String): Result<WheelOfFortuneConfigData>
    suspend fun show(token: String): Result<Unit>
    suspend fun roll(token: String): Result<GiftId>
    suspend fun makeDistribution(token: String, giftId: GiftId, email: Email): Result<Unit>
}
