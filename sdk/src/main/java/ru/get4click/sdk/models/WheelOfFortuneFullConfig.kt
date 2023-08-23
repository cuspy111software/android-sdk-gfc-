package ru.get4click.sdk.models

import androidx.annotation.ColorInt
import ru.get4click.sdk.data.models.GiftId

internal data class WheelOfFortuneFullConfig(
    val token: String,
    val openTimeout: Long,
    val forceShow: Boolean,
    val wofModalAppearance: WofModalAppearance
)

internal data class WofModalAppearance(
    val wheelAppearance: WheelAppearance,
    val title: String,
    val subtitle: String,
    val buttonText: String,
    val winningTitle: String,
    val textAfterWin: String,
    val loseTitle: String,
    val textAfterLose: String,
    @ColorInt val buttonSpinColor: Int,
    @ColorInt val buttonTextColor: Int,
    @ColorInt val buttonGiftColor: Int,
    @ColorInt val buttonGiftBackColor: Int,
    @ColorInt val leftGiftColor: Int,
    @ColorInt val rightGiftColor: Int,
    @ColorInt val modalBackgroundColor: Int
)

internal data class WheelAppearance(
    val items: List<WheelItem>,
    @ColorInt val wheelColor: Int
)

internal data class WheelItem(
    val giftId: GiftId,
    val title: String,
    @ColorInt val backgroundColor: Int,
    @ColorInt val textColor: Int
)
