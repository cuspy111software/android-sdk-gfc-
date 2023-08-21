package ru.get4click.sdk.data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.InlineDataPart
import com.github.kittinunf.fuel.json.responseJson
import org.json.JSONException
import ru.get4click.sdk.BuildConfig
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
            .get("${BuildConfig.BASE_API_URL}${promoCodeCreds.apiKey}/coupon-code/active")
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

    override suspend fun promoCodeIsAlreadyUsed(
        promoCodeCreds: PromoCodeCreds,
        couponCodeDistributionId: Int
    ): Result<Unit> {
        val (_, _, result) = Fuel
            .upload("${BuildConfig.BASE_API_URL}${promoCodeCreds.apiKey}/coupon-code/used")
            .add(InlineDataPart(promoCodeCreds.email.value, "email"))
            .add(InlineDataPart(couponCodeDistributionId.toString(), "distributionId"))
            .responseJson()

        return result.fold(
            success = { responseData ->
                val jsonResp = responseData.obj()
                if (jsonResp.isStatusOk()) {
                    Result.success(Unit)
                } else {
                    Result.failure(Get4ClickApiException(jsonResp.optString("error")))
                }
            },
            failure = { e -> Result.failure(e.exception) }
        )
    }
}
