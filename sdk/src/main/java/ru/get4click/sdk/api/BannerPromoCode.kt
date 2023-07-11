package ru.get4click.sdk.api

import android.widget.ImageView

interface BannerPromoCode {
    fun show()

    /**
     * Returns [ImageView] button that can be used to show Promo Code Banner. Returns null if
     * the banner is not initialized yet
     */
    fun getButton(): ImageView
}
