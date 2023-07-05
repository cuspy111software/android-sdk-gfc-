package ru.get4click.sdk.api

interface BannerPromoCodeListener {
    fun onInit() { }
    fun onInitFailed(e: Throwable) { }
}
