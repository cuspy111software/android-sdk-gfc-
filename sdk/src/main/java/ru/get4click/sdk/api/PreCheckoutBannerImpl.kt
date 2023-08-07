package ru.get4click.sdk.api

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.get4click.sdk.data.PreCheckoutApi
import ru.get4click.sdk.models.PreCheckoutModel
import ru.get4click.sdk.ui.precheckout.PreCheckoutViewWrapper

internal class PreCheckoutBannerImpl(
    private val activity: ComponentActivity,
    private val apiKey: String,
    private val shopId: Int,
    private val preCheckoutApi: PreCheckoutApi,
    private val preCheckoutListener: PreCheckoutListener
) : PreCheckoutBanner {
    private var preCheckoutModel: PreCheckoutModel? = null

    private var preCheckoutViewWrapper: PreCheckoutViewWrapper? = null

    private val scope = activity.lifecycleScope

    init {
        scope.launch(Dispatchers.IO) {
            preCheckoutApi.getPreCheckoutData(apiKey, shopId).onSuccess { preCheckoutModel ->
                    this@PreCheckoutBannerImpl.preCheckoutModel = PreCheckoutModel(
                        widgetId = preCheckoutModel.widgetId,
                        baseColor = preCheckoutModel.base_colour,
                        messages = preCheckoutModel.messages,
                    )
                    withContext(Dispatchers.Main) {
                        preCheckoutListener.onInit()
                        this@PreCheckoutBannerImpl.preCheckoutModel?.let { data ->
                            preCheckoutViewWrapper = PreCheckoutViewWrapper.make(
                                    activity.window.decorView.rootView,
                                    data
                                ) { close() }
                            preCheckoutViewWrapper?.show()
                        }
                    }
                }.onFailure { e ->
                    withContext(Dispatchers.Main) { preCheckoutListener.onInitFailed() }
                    Log.e(TAG, e.message ?: "")
                }
        }
    }

    private fun close() {
        scope.launch(Dispatchers.IO) {
            preCheckoutApi.sendNotifyClose(
                apiKey, preCheckoutModel?.widgetId ?: 1, ACTION_CLOSE
            ).onSuccess {
                preCheckoutListener.onClose()
            }.onFailure { /* no-op */ }
        }
    }

    override fun show() {
        preCheckoutViewWrapper?.show()
    }

    companion object {
        private const val TAG = "Precheckout"
        private const val ACTION_CLOSE = "close"
    }
}