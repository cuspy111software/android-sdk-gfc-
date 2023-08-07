package ru.get4click.sdk.data.utlis

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import ru.get4click.sdk.data.models.precheckout.PreCheckoutItemData
import ru.get4click.sdk.data.models.precheckout.PreCheckoutApiModel
import ru.get4click.sdk.data.models.precheckout.PreCheckoutCloseApiModel

internal fun JSONObject.parseToModelPreCheckout(): PreCheckoutApiModel {
    val data = this.getJSONObject("data")
    val itemsJson = (JSONTokener(data.getString("messages")).nextValue() as JSONArray)

    val length = itemsJson.length()
    val items = buildList {
        for (i in 0 until length) {
            val item = itemsJson.getJSONObject(i)
            val preCheckoutItemData = PreCheckoutItemData(
                id          = item.getInt("id"),
                title       = item.getString("title"),
                description = item.getString("description"),
                aboutUrl    = item.optString("about_url").takeIf { it.isNotEmpty() },
                icon        = item.optString("logo").takeIf { it != "null" && it.isNotEmpty() }
            )
            add(preCheckoutItemData)
        }
    }

    return PreCheckoutApiModel(
        widgetId    = data.getInt("widget_id"),
        base_colour = data.getString("base_colour"),
        messages    = items
    )
}

internal fun JSONObject.parseToModelClosePreCheckout(): PreCheckoutCloseApiModel {
    return PreCheckoutCloseApiModel(
        data    = getString("data"),
        status = getString("status"),
        error = getString("error")
    )
}
