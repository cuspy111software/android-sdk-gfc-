package ru.get4click.sdk.api

import android.widget.ImageView

interface WheelOfFortune {
    /**
     * Show the Wheel Of Fortune
     */
    fun show()

    /**
     * Returns [ImageView] button that can be used to show the Wheel Of Fortune. Returns null if
     * the wheel is not initialized yet
     */
    fun getButton(): ImageView?
}
