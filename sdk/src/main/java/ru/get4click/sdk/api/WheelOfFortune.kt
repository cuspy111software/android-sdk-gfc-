package ru.get4click.sdk.api

import android.content.Context

interface WheelOfFortune {
    val ready: Boolean
    fun show(context: Context)
}
