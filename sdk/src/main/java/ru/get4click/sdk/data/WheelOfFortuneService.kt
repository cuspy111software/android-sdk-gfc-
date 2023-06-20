package ru.get4click.sdk.data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.json.responseJson
import ru.get4click.sdk.data.models.GiftId

internal class WheelOfFortuneService : WheelOfFortuneApi {
    override suspend fun initialize(token: String): Result<Unit> {
        val (_, _, result) = Fuel
            .post("https://get4click.ru/api/wheel-of-fortune/show/")
            .jsonBody(body = "{ \"token\" : \"$token\"}")
            .response()

        return result.fold(
            success = { Result.success(Unit) },
            failure = { Result.failure(Exception()) }
        )
    }

    override suspend fun roll(token: String): Result<GiftId> {
        val (_, _, result) = Fuel
            .post("https://get4click.ru/api/wheel-of-fortune/roll/")
            .jsonBody(body = "{ \"token\" : \"$token\" }")
            .responseJson()


        return result.fold(
            success = { res -> Result.success(GiftId(res.obj().getInt("gift_id"))) },
            failure = { Result.failure(Exception()) }
        )
    }

    override suspend fun makeDistribution(token: String, giftId: GiftId): Result<Unit> {
        val (_, _, result) = Fuel
            .post("https://get4click.ru/api/wheel-of-fortune/make-distribution/")
            .jsonBody(body = "{ \"token\":\"$token\", \"gift_id\":\"$giftId\" }")
            .responseJson()

        return result.fold(
            success = { Result.success(Unit) },
            failure = { Result.failure(Exception()) }
        )
    }
}