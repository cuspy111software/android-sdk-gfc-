package ru.get4click.sdk.ui.precheckout

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.get4click.sdk.models.PreCheckoutModel

internal class PreCheckoutViewWrapper(
    viewGroup: ViewGroup,
    content: PreCheckoutView,
    context: Context,
    onCloseClicked: () -> Unit,
) : BaseTransientBottomBar<PreCheckoutViewWrapper>(context, viewGroup, content, content) {

    init {
        view.setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.transparent))
        view.setPadding(0, 0, 0, 0)
        content.onCloseClicked = {
            dismiss()
            onCloseClicked.invoke()
        }
    }

    companion object {
        private const val DEFAULT_BOTTOM_PADDING = 120

        fun make(view: View, preCheckoutModel: PreCheckoutModel, onCloseClicked: () -> Unit): PreCheckoutViewWrapper {
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            val customView = PreCheckoutView(view.context, preCheckoutModel)

            return PreCheckoutViewWrapper(parent, customView, view.context, onCloseClicked).apply {
                duration = LENGTH_INDEFINITE
                val layout = this.view as Snackbar.SnackbarLayout
                layout.setPadding(0, 0, 0, DEFAULT_BOTTOM_PADDING)
            }
        }

        private fun View?.findSuitableParent(): ViewGroup? {
            var view = this
            var fallback: ViewGroup? = null
            do {
                if (view is CoordinatorLayout) {
                    return view
                } else if (view is FrameLayout) {
                    if (view.id == android.R.id.content) {
                        return view
                    } else {
                        fallback = view
                    }
                }
                if (view != null) {
                    val parent = view.parent
                    view = if (parent is View) parent else null
                }
            } while (view != null)
            return fallback
        }
    }
}
