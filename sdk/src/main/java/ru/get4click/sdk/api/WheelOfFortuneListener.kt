package ru.get4click.sdk.api

interface WheelOfFortuneListener {
    fun onInit(wheelOfFortune: WheelOfFortune) { }
    fun onInitFailed(e: Throwable) { }
    fun onShow() { }
    fun onShowFailed(e: Throwable) { }
}
