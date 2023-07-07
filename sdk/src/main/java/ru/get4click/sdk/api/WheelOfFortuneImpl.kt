package ru.get4click.sdk.api

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.get4click.sdk.Get4ClickSDK
import ru.get4click.sdk.R
import ru.get4click.sdk.data.WheelOfFortuneApi
import ru.get4click.sdk.data.models.Email
import ru.get4click.sdk.data.models.GiftId
import ru.get4click.sdk.models.WheelOfFortuneFullConfig
import ru.get4click.sdk.ui.wheeloffortune.WheelOfFortuneDialog
import ru.get4click.sdk.ui.wheeloffortune.WheelOfFortuneDialogInteractor
import ru.get4click.sdk.utils.toUiModel

internal class WheelOfFortuneImpl(
    private val activity: ComponentActivity,
    private val apiKey: String,
    private val wheelOfFortuneApi: WheelOfFortuneApi,
    private val wheelOfFortuneListener: WheelOfFortuneListener
) : WheelOfFortune, WheelOfFortuneDialogInteractor {

    private var wheelOfFortuneDialog: WheelOfFortuneDialog? = null
    private var wheelConfig: WheelOfFortuneFullConfig? = null

    init {
        activity.lifecycleScope.launch(Dispatchers.IO) {
            wheelOfFortuneApi.init(apiKey)
                .onFailure { e ->
                    Log.e(Get4ClickSDK.TAG, "Wheel Of Fortune failed on INIT step")
                    Log.i(Get4ClickSDK.TAG, e.stackTraceToString())
                    withContext(Dispatchers.Main) { wheelOfFortuneListener.onInitFailed(e) }

                }
                .onSuccess {
                    val config = it.toUiModel()
                    wheelConfig = config

                    if (config.forceShow) {
                        delay(config.openTimeout)
                        requestShow(config)
                    }

                    withContext(Dispatchers.Main) {
                        wheelOfFortuneListener.onInit(this@WheelOfFortuneImpl)
                    }
                }
        }
    }

    override fun show() {
        val config = wheelConfig ?: return

        wheelOfFortuneDialog = WheelOfFortuneDialog(
            context            = activity,
            themeResId         = R.style.SdkMaterialTheme,
            wofModalAppearance = config.wofModalAppearance,
            isReady            = false,
            wofInteractor      = this
        )

        wheelOfFortuneDialog?.show()

        activity.lifecycleScope.launch(Dispatchers.IO) {
            wheelOfFortuneApi.show(config.token)
                .onFailure { e ->
                    withContext(Dispatchers.Main) {
                        wheelOfFortuneDialog
                            ?.showError(activity.getString(R.string.general_error_text))
                        Log.e(Get4ClickSDK.TAG, "Wheel Of Fortune failed on SHOW step")
                        Log.i(Get4ClickSDK.TAG, e.stackTraceToString())
                        wheelOfFortuneListener.onShowFailed(e)
                    }
                }
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        wheelOfFortuneDialog?.setReady()
                        wheelOfFortuneListener.onShow()
                    }
                }
        }
    }

    private suspend fun requestShow(config: WheelOfFortuneFullConfig) {
        wheelOfFortuneApi.show(config.token)
            .onFailure { e ->
                Log.e(Get4ClickSDK.TAG, "Wheel Of Fortune failed on SHOW step")
                Log.i(Get4ClickSDK.TAG, e.stackTraceToString())
            }
            .onSuccess {
                withContext(Dispatchers.Main) {
                    wheelOfFortuneDialog = WheelOfFortuneDialog(
                        context            = activity,
                        themeResId         = android.R.style.Theme_NoTitleBar_Fullscreen,
                        wofModalAppearance = config.wofModalAppearance,
                        isReady            = true,
                        wofInteractor      = this@WheelOfFortuneImpl
                    )

                    wheelOfFortuneDialog?.show()
                }
            }
    }

    override suspend fun spin(): Result<GiftId> {
        val config = wheelConfig
            ?: return Result.failure(IllegalStateException("Wheel Of Fortune is not initialized"))

        return wheelOfFortuneApi.roll(config.token)
    }

    override suspend fun makeDistribution(giftId: GiftId, email: Email): Result<Unit> {
        val config = wheelConfig
            ?: return Result.failure(IllegalStateException("Wheel Of Fortune is not initialized"))

        return wheelOfFortuneApi.makeDistribution(config.token, giftId, email)
    }
}
