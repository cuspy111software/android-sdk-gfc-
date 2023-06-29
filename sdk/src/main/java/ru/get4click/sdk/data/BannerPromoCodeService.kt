package ru.get4click.sdk.data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import org.json.JSONException
import org.json.JSONObject
import ru.get4click.sdk.data.models.promocode.PromoCodeApiModel
import ru.get4click.sdk.data.utlis.parseToModel
import ru.get4click.sdk.ui.bannerpromocode.PromoCodeCreds

internal class BannerPromoCodeService : BannerPromoCodeApi {
    override suspend fun getPromoCodeData(
        promoCodeCreds: PromoCodeCreds
    ): Result<PromoCodeApiModel> {
        val (_, _, result) = Fuel
            .get("https://staging.get4click.ru/api/${promoCodeCreds.apiKey}/coupon-code/active")
            .apply { parameters = listOf("email" to promoCodeCreds.email.value) }
            .responseJson()

        return result.fold(
            success = { responseData ->
                try {
                    val jsonResult = responseData.obj()
                    if (jsonResult.isStatusOk()) {
                        val data = responseData.obj().getJSONObject("data")
                        Result.success(data.parseToModel())
                    } else {
                        Result.failure(Exception())
                    }
                } catch (e: JSONException) {
                    Result.failure(e)
                }
            },
            failure = { Result.failure(it.exception) }
        )
    }

    private fun JSONObject.isStatusOk(): Boolean {
        return getString("status").equals("OK", ignoreCase = false)
    }
}
