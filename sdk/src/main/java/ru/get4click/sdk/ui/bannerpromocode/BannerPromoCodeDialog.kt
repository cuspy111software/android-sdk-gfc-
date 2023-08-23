package ru.get4click.sdk.ui.bannerpromocode

import android.app.Dialog
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import ru.get4click.sdk.ui.wheeloffortune.BannerPromoCodeInteractor

internal abstract class BannerPromoCodeDialog(
    context: Context,
    protected val bannerDialogInteractor: BannerPromoCodeInteractor
) : Dialog(context) {

    protected val dialogScope = CoroutineScope(SupervisorJob())

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dialogScope.cancel()
    }
}
