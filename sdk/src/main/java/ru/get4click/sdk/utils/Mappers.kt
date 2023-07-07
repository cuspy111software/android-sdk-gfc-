package ru.get4click.sdk.utils

import android.graphics.Color
import ru.get4click.sdk.data.models.GiftId
import ru.get4click.sdk.data.models.wheel.WheelItemData
import ru.get4click.sdk.data.models.wheel.WheelOfFortuneConfigData
import ru.get4click.sdk.models.WheelAppearance
import ru.get4click.sdk.models.WheelItem
import ru.get4click.sdk.models.WheelOfFortuneFullConfig
import ru.get4click.sdk.models.WofModalAppearance

internal fun WheelOfFortuneConfigData.toUiModel(): WheelOfFortuneFullConfig {
    return WheelOfFortuneFullConfig(
        token                = token,
        openTimeout          = openTimeout.toLong(),
        forceShow            = forcedView == 1,
        wofModalAppearance   = WofModalAppearance(
            title                = title,
            subtitle             = subTitle,
            buttonText           = buttonText,
            winningTitle         = winningTitle,
            textAfterWin         = textAfterWin,
            loseTitle            = loseTitle,
            textAfterLose        = textAfterLose,
            buttonSpinColor      = Color.parseColor(buttonSpinWheelColor),
            buttonTextColor      = Color.parseColor(buttonTextColor),
            buttonGiftColor      = Color.parseColor(buttonSettings.buttonGiftColor),
            leftGiftColor        = Color.parseColor(giftLeftColor),
            rightGiftColor       = Color.parseColor(giftRightColor),
            modalBackgroundColor = Color.parseColor(backgroundModalColor),
            wheelAppearance      = WheelAppearance(
                items      = items.map { it.toUiModel() }.sortedBy { it.giftId.id },
                wheelColor = Color.parseColor(rollColor)
            )
        )
    )
}

internal fun WheelItemData.toUiModel(): WheelItem {
    return WheelItem(
        giftId              = GiftId(id),
        title           = title,
        backgroundColor = Color.parseColor(backgroundColor),
        textColor       = Color.parseColor(textColor),
    )
}
