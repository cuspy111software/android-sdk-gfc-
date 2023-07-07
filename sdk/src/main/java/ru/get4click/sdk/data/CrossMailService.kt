package ru.get4click.sdk.data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import org.json.JSONException
import ru.get4click.sdk.data.models.Email
import ru.get4click.sdk.data.models.Get4ClickApiException
import ru.get4click.sdk.data.utlis.isStatusOk

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
                try {
                    val jsonResp = resp.obj()
                    if (jsonResp.isStatusOk()) {
                        Result.success(Unit)
                    } else {
                        val errorMsg = jsonResp.optString("error")
                        Result.failure(Get4ClickApiException(errorMsg))
                    }
                } catch (e: JSONException) {
                    Result.failure(e)
                }
            },
            failure = {  e ->
                Result.failure(e)
            }
        )
    }
}
