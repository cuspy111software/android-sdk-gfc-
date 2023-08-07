package ru.get4click.sdk.data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import org.json.JSONException
import ru.get4click.sdk.data.models.Get4ClickApiException
import ru.get4click.sdk.data.models.precheckout.PreCheckoutApiModel
import ru.get4click.sdk.data.models.precheckout.PreCheckoutCloseApiModel
import ru.get4click.sdk.data.utlis.isStatusOk
import ru.get4click.sdk.data.utlis.parseToModelClosePreCheckout
import ru.get4click.sdk.data.utlis.parseToModelPreCheckout

internal class PreCheckoutService : PreCheckoutApi {
    override suspend fun getPreCheckoutData(
        apiKey: String,
        shopId: Int
    ): Result<PreCheckoutApiModel> {
        val (_, _, result) = Fuel
            .get("https://get4click.ru/api/$apiKey/precheckout/init/")
            .apply {
                parameters = listOf(
                    "is_mobile" to 1,
                    "page_url" to "https://test.get4click.ru/",
                    "shop_id" to shopId
                )
            }
            .responseJson()

        return result.fold(
            success = { data ->
                try {
                    val jsonObj = data.obj()
                    if (jsonObj.isStatusOk()) {
                        val preCheckoutApiModel = jsonObj.parseToModelPreCheckout()
                        Result.success(preCheckoutApiModel)
                    } else {
                        Result.failure(Get4ClickApiException(jsonObj.optString("error")))
                    }
                } catch (e: JSONException) {
                    Result.failure(e)
                }
            },
            failure = { e -> Result.failure(e) }
        )
    }

    override suspend fun sendNotifyClose(
        apiKey: String,
        widgetId: Int,
        userAction: String
    ): Result<PreCheckoutCloseApiModel> {
        val (_, _, result) = Fuel
            .post("https://staging.get4click.ru/api/$apiKey/precheckout/action/")
            .apply {
                parameters = listOf(
                    "user_action" to userAction,
                    "widget_id" to widgetId
                )
            }
            .responseJson()
        return result.fold(
            success = { data ->
                try {
                    val jsonObj = data.obj()
                    if (jsonObj.isStatusOk()) {
                        val preCheckoutApiModel = jsonObj.parseToModelClosePreCheckout()
                        Result.success(preCheckoutApiModel)
                    } else {
                        Result.failure(Get4ClickApiException(jsonObj.optString("error")))
                    }
                } catch (e: JSONException) {
                    Result.failure(e)
                }
            },
            failure = { e -> Result.failure(e) }
        )
    }
}
