package ru.get4click.sdk.api

import android.R
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.get4click.sdk.data.WheelOfFortuneApi
import ru.get4click.sdk.ui.wheeloffortune.WheelOfFortuneConfig
import ru.get4click.sdk.ui.wheeloffortune.WheelOfFortuneDialog

internal class WheelOfFortuneImpl(
    private val scope: CoroutineScope,
    private val token: String,
    private val autoOpenTimeout: Long? = null,
    private val config: WheelOfFortuneConfig,
    private val wheelOfFortuneApi: WheelOfFortuneApi,
    private val onReady: () -> Unit
) : WheelOfFortune {

    private var _ready: Boolean = false
    override val ready: Boolean get() = _ready

    private var wheelOfFortuneDialog: WheelOfFortuneDialog? = null

    init {
        scope.launch {
            wheelOfFortuneApi.initialize(token)
                .onFailure { _ready = false }
                .onSuccess {
                    wheelOfFortuneApi.roll(token)
                        .onFailure { _ready = false }
                        .onSuccess {
                            _ready = true
                            onReady.invoke()
                        }
                }
        }
    }

    override fun show(context: Context) {
        if (_ready) {
            wheelOfFortuneDialog = WheelOfFortuneDialog(
                context,
                R.style.Theme_NoTitleBar_Fullscreen,
                listOf()
            )
        }
    }

}
