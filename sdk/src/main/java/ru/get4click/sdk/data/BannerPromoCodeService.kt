package ru.get4click.sdk.data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import org.json.JSONException
import ru.get4click.sdk.data.models.Get4ClickApiException
import ru.get4click.sdk.data.models.promocode.PromoCodeApiModel
import ru.get4click.sdk.data.utlis.isStatusOk
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
                    val jsonResp = responseData.obj()
                    if (jsonResp.isStatusOk()) {
                        val data = responseData.obj().getJSONObject("data")
                        Result.success(data.parseToModel())
                    } else {
                        Result.failure(Get4ClickApiException(jsonResp.optString("error")))
                    }
                } catch (e: JSONException) {
                    Result.failure(e)
                }
            },
            failure = { Result.failure(it.exception) }
        )
    }
}
