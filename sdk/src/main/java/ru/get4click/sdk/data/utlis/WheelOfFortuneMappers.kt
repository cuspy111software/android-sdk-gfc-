package ru.get4click.sdk.data.utlis

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import ru.get4click.sdk.data.models.wheel.ButtonSettings
import ru.get4click.sdk.data.models.wheel.WheelItemData
import ru.get4click.sdk.data.models.wheel.WheelOfFortuneConfigData

internal fun JSONObject.parseToWheelOfFortuneConfig(): WheelOfFortuneConfigData {
    val wheelJson = this.getJSONObject("data").getJSONObject("shopWOF")
    val buttonSettingsJson = wheelJson.getJSONObject("buttonSettings")
    val wheelSettingsColors = wheelJson
            .getJSONObject("wheelSettings")
            .getJSONObject("colors")

    val buttonSettings = ButtonSettings(
        type                      = buttonSettingsJson.getString("type"),
        position                  = buttonSettingsJson
                                        .getJSONObject("position")
                                        .getString("common"),
        buttonGiftColor           = buttonSettingsJson
                                        .getJSONObject("colors")
                                        .getString("buttonGiftColor"),
        buttonGiftBackgroundColor = buttonSettingsJson
                                        .getJSONObject("colors")
                                        .getString("buttonGiftBackgroundColor"),
        buttonSize                = buttonSettingsJson
                                        .getJSONObject("buttonSize")
                                        .getString("buttonGiftParams")
    )

    val itemsJson = (JSONTokener(wheelJson.getString("items")).nextValue() as JSONArray)
    val length = itemsJson.length()
    val items = buildList {
        for (i in 0 until length) {
            val item = itemsJson.getJSONObject(i)
            val wheelItemData = WheelItemData(
                id              = item.getInt("id"),
                title           = item.getString("title"),
                backgroundColor = item.getString("background_color"),
                textColor       = item.getString("text_color"),
            )
            add(wheelItemData)
        }
    }

    return WheelOfFortuneConfigData(
        emailStep            = wheelJson.getInt("emailStep"),
        token                = wheelJson.getString("token"),
        openTimeout          = wheelJson.getInt("openTimeOut"),
        title                = wheelJson.getString("title"),
        subTitle             = wheelJson.getString("subTitle"),
        buttonText           = wheelJson.getString("buttonText"),
        winningTitle         = wheelJson.getString("winningTitle"),
        textAfterWin         = wheelJson.getString("textAfterWin"),
        loseTitle            = wheelJson.getString("loseTitle"),
        textAfterLose        = wheelJson.getString("textAfterLose"),
        buttonTextColor      = wheelJson.getString("buttonTextColor"),
        items                = items,
        rollColor            = wheelSettingsColors.getString("rollColor"),
        giftRightColor       = wheelSettingsColors.getString("giftRightColor"),
        giftLeftColor        = wheelSettingsColors.getString("giftLeftColor"),
        backgroundModalColor = wheelSettingsColors.getString("backgroundModalColor"),
        forcedView           = wheelJson.getInt("forcedView"),
        buttonSpinWheelColor = wheelJson.getString("buttonSpinWheelColor"),
        buttonSettings       = buttonSettings
    )
}
