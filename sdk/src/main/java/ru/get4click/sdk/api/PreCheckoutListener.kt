package ru.get4click.sdk.api

interface PreCheckoutListener {
    fun onInit() { }
    fun onInitFailed() { }
    fun onClose() { }
}
