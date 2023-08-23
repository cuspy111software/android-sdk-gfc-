package ru.get4click.sdk.api

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.get4click.sdk.data.CrossMailApi
import ru.get4click.sdk.data.models.Email
import ru.get4click.sdk.models.CrossMailStatus

internal class CrossMailImpl(
    private val crossMailApi: CrossMailApi,
    private val scope: CoroutineScope,
    private val apiKey: String
) : CrossMail {
    override fun sendStatus(
        email: String,
        status: CrossMailStatus,
        resultListener: ResultListener
    ) {
        sendStatus(email, status.value, resultListener)
    }

    override fun sendStatus(email: String, status: Int, resultListener: ResultListener) {
        scope.launch(Dispatchers.IO) {
            crossMailApi.sendEmail(apiKey, Email(email), status)
                .onSuccess {
                    withContext(Dispatchers.Main) { resultListener.onSuccess() }
                }
                .onFailure { e ->
                    withContext(Dispatchers.Main) { resultListener.onFailure(e) }
                    Log.i(TAG, e.message ?: "")
                }
        }
    }

    companion object {
        private const val TAG = "Cross Mail"
    }
}
