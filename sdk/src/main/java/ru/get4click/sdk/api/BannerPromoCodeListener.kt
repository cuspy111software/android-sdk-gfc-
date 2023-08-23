package ru.get4click.sdk.api

interface BannerPromoCodeListener {
    fun onInit(bannerPromoCode: BannerPromoCode) { }
    fun onInitFailed(e: Throwable) { }
}
