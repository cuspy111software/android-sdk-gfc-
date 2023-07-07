package ru.get4click.sdk.data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import org.json.JSONObject
import ru.get4click.sdk.data.models.Email
import ru.get4click.sdk.data.models.Get4ClickApiException

internal class CrossMailService : CrossMailApi {
    override suspend fun sendEmail(
        apiKey: String,
        email: Email,
        status: Int
    ): Result<Unit> {
        val (_, _, result) = Fuel
            .post("https://staging.get4click.ru/api/$apiKey/write-mobile-client/")
            .apply { parameters = listOf("email" to email.value, "status" to status) }
            .responseJson()

        return result.fold(
            success = { resp ->
                val jsonResp = resp.obj()
                if (jsonResp.isStatusOk()) {
                    Result.success(Unit)
                } else {
                    val errorMsg = jsonResp.optString("error")
                    Result.failure(Get4ClickApiException(errorMsg))
                }
            },
            failure = {  e ->
                Result.failure(e)
            }
        )
    }

    private fun JSONObject.isStatusOk(): Boolean {
        return getString("status").equals("OK", ignoreCase = false)
    }
}
