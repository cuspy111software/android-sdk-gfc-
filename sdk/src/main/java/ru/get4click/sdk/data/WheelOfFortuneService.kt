package ru.get4click.sdk.data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.InlineDataPart
import com.github.kittinunf.fuel.json.responseJson
import org.json.JSONException
import org.json.JSONObject
import ru.get4click.sdk.data.models.Email
import ru.get4click.sdk.data.models.GiftId
import ru.get4click.sdk.data.models.wheel.WheelOfFortuneConfigData
import ru.get4click.sdk.data.utlis.parseToWheelOfFortuneConfig

internal class WheelOfFortuneService : WheelOfFortuneApi {
    override suspend fun init(apiKey: String): Result<WheelOfFortuneConfigData> {
        val (_, _, result) = Fuel
            .get("https://staging.get4click.ru/api/$apiKey/wheel-of-fortune/init/")
            .responseJson()

        return result.fold(
            success = { data ->
                try {
                    val jsonObj = data.obj()
                    if (jsonObj.isStatusOk()) {
                        val wheelConfig = jsonObj.parseToWheelOfFortuneConfig()
                        Result.success(wheelConfig)
                    } else {
                        Result.failure(Exception(jsonObj.optString("error")))
                    }
                } catch (e: JSONException) {
                    Result.failure(e)
                }
            },
            failure = { e -> Result.failure(e) }
        )
    }

    override suspend fun show(token: String): Result<Unit> {
        val (_, _, result) = Fuel
            .upload("https://staging.get4click.ru/api/wheel-of-fortune/show/")
            .add(InlineDataPart(token, "token"))
            .responseJson()

        return result.fold(
            success = { res ->
                val jsonRes = res.obj()
                if (jsonRes.isStatusOk()) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(jsonRes.optString("error")))
                }
            },
            failure = { e -> Result.failure(e) }
        )
    }

    override suspend fun roll(token: String): Result<GiftId> {
        val (_, _, result) = Fuel
            .upload("https://staging.get4click.ru/api/wheel-of-fortune/roll/")
            .add(InlineDataPart(token, "token"))
            .responseJson()


        return result.fold(
            success = { res ->
                val jsonRes = res.obj()
                if (jsonRes.isStatusOk()) {
                    Result.success(GiftId(res.obj().optInt("gift_id")))
                } else {
                    Result.failure(Exception(jsonRes.optString("error")))
                }
            },
            failure = { e -> Result.failure(e) }
        )
    }

    override suspend fun makeDistribution(
        token: String,
        giftId: GiftId,
        email: Email
    ): Result<Unit> {
        val (_, _, result) = Fuel
            .upload("https://staging.get4click.ru/api/wheel-of-fortune/make-distribution/")
            .add(InlineDataPart(token, "token"))
            .add(InlineDataPart(giftId.id.toString(), "gift_id"))
            .add(InlineDataPart(email.value, "email"))
            .responseJson()

        return result.fold(
            success = { res ->
                val jsonRes = res.obj()
                if (jsonRes.isStatusOk()) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(jsonRes.optString("error")))
                }
            },
            failure = { e -> Result.failure(e) }
        )
    }

    private fun JSONObject.isStatusOk(): Boolean {
        return getString("status").equals("OK", ignoreCase = false)
    }
}
