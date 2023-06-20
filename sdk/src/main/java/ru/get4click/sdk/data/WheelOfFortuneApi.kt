package ru.get4click.sdk.data

import ru.get4click.sdk.data.models.GiftId

internal interface WheelOfFortuneApi {
    suspend fun initialize(token: String): Result<Unit>
    suspend fun roll(token: String): Result<GiftId>
    suspend fun makeDistribution(token: String, giftId: GiftId): Result<Unit>
}
