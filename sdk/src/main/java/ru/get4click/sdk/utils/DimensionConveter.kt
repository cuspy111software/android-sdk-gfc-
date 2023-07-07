package ru.get4click.sdk.utils

import android.content.res.Resources
import android.util.TypedValue

internal fun dpToPx(dp: Float, r: Resources): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        r.displayMetrics
    )
}

internal fun spToPx(sp: Float, r: Resources): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        r.displayMetrics
    )
}
