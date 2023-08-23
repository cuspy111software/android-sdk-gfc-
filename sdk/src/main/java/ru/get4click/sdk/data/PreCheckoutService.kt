package ru.get4click.sdk.data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import org.json.JSONException
import ru.get4click.sdk.BuildConfig
import ru.get4click.sdk.data.models.Get4ClickApiException
import ru.get4click.sdk.data.models.precheckout.PreCheckoutApiModel
import ru.get4click.sdk.data.models.precheckout.PreCheckoutCloseApiModel
import ru.get4click.sdk.data.utlis.isStatusOk
import ru.get4click.sdk.data.utlis.parseToModelClosePreCheckout
import ru.get4click.sdk.data.utlis.parseToModelPreCheckout

internal class PreCheckoutService : PreCheckoutApi {
    override suspend fun getPreCheckoutData(
        apiKey: String,
    ): Result<PreCheckoutApiModel> {
        val (_, _, result) = Fuel
            .get("${BuildConfig.BASE_API_URL}$apiKey/precheckout/mobile-init/")
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
        sessionId: String,
        userAction: String,
    ): Result<PreCheckoutCloseApiModel> {
        val (_, _, result) = Fuel
            .post("${BuildConfig.BASE_API_URL}$apiKey/precheckout/mobile-action/")
            .apply {
                parameters = listOf(
                    "user_action" to userAction,
                    "widget_id" to widgetId,
                    "session_id" to sessionId
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
