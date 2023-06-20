package ru.get4click.sdk.ui.wheeloffortune

import androidx.annotation.ColorInt

data class WheelOfFortuneConfig(
    val borderStrokeWidth: Float,
    @ColorInt val borderStrokeColor: Int,
    val textSize: Float,
    val pivotCircleStrokeWidth: Float,
    @ColorInt val pivotCircleBorderColor: Int,
    @ColorInt val pivotCircleFillingColor: Int
)
