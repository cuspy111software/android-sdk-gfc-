package ru.get4click.sdk.ui.bannerpromocode

import android.app.Dialog
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

internal abstract class BannerPromoCodeDialog(context: Context,) : Dialog(context) {
    protected val dialogScope = CoroutineScope(SupervisorJob())

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dialogScope.cancel()
    }
}
