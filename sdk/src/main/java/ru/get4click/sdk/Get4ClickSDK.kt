package ru.get4click.sdk

import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.get4click.sdk.api.WheelOfFortune
import ru.get4click.sdk.api.WheelOfFortuneImpl
import ru.get4click.sdk.data.WheelOfFortuneService
import ru.get4click.sdk.models.Banner
import ru.get4click.sdk.models.Order

object Get4ClickSDK {

    internal val TAG = "Get4Click"

    private var shopId = 0
    private var orderMap: MutableMap<String, Order> = HashMap<String, Order>()

    private val sdkScope = CoroutineScope(SupervisorJob())

    fun getShopId(): Int {
        return shopId
    }


    private  fun intSDKErrors(){
        shopIdError()
    }
    private fun shopIdError() {
        if (getShopId() == 0) {
            throw IllegalArgumentException("You have to set shopId first in initSDK() method")
        }
    }



    fun getBannerWithCurrentOrder(bannerId : Int) : Banner{
        intSDKErrors()
        return Banner(bannerId, getCurrentOrder())
    }


    fun getCurrentOrder(): Order {
        return orderMap.get("current")!!
    }

    fun addOrder(name: String, id: Order) {
        orderMap.put(name, id)
    }

    fun resetCurrentOrder() {
        orderMap.put("current", Order())
    }

    fun removeOrder(name: String) {
        orderMap.remove(name)
    }

    fun getOrderBy(name: String): Order? {
        return orderMap.get(name)
    }

    fun clearOrders() {
        orderMap.clear()
    }


    fun initSdk(shopId: Int) {
        this.shopId = shopId
        orderMap.put("current", Order())
    }

    /**
     * Creates an instance of [WheelOfFortune]
     *
     * @param activity the host activity
     * @param apiKey client API key
     * @param onInit callback triggered when the WOF is initialized
     * @param onInitFailed callback triggered when WOF initialization failed
     */
    fun createWheelOfFortune(
        activity: ComponentActivity,
        apiKey: String,
        onInit: (WheelOfFortune) -> Unit = { },
        onInitFailed: (e: Throwable) -> Unit = { }
    ): WheelOfFortune {
        return WheelOfFortuneImpl(
            activity          = activity,
            apiKey            = apiKey,
            wheelOfFortuneApi = WheelOfFortuneService(),
            onInit            = onInit,
            onInitFailed      = onInitFailed
        )
    }
}