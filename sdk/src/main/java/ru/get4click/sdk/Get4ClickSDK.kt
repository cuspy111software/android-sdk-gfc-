package ru.get4click.sdk

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.get4click.sdk.api.WheelOfFortune
import ru.get4click.sdk.api.WheelOfFortuneImpl
import ru.get4click.sdk.data.WheelOfFortuneService
import ru.get4click.sdk.models.Banner
import ru.get4click.sdk.models.Order
import ru.get4click.sdk.ui.wheeloffortune.WheelOfFortuneConfig
import ru.get4click.sdk.view.FortuneWheelView
import kotlin.coroutines.CoroutineContext

object Get4ClickSDK {
    
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
     * @param autoOpenTimeout Wheel of fortune is opening after [autoOpenTimeout] milliseconds
     * automatically. Optional parameter. You can show the wheel with help of [WheelOfFortune.show]
     */
    fun createWheelOfFortune(
        context: Context,
        viewConfig: WheelOfFortuneConfig,
        autoOpenTimeout: Long? = null
    ): WheelOfFortune {
        return WheelOfFortuneImpl(
            scope = sdkScope,
            token = shopId.toString(),
            autoOpenTimeout = autoOpenTimeout,
            config = viewConfig,
            wheelOfFortuneApi = WheelOfFortuneService()
        ) {

        }
    }
}