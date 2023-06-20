package ru.get4click.sdk.data.models.wheel

internal data class WheelOfFortuneConfigData(
    val emailStep: Int,
    val token: String,
    val openTimeout: Int,
    val title: String,
    val subTitle: String,
    val buttonText: String,
    val winningTitle: String,
    val textAfterWin: String,
    val loseTitle: String,
    val textAfterLose: String,
    val buttonTextColor: String,
    val items: List<WheelItemData>,
    val rollColor: String,
    val giftRightColor: String,
    val giftLeftColor: String,
    val backgroundModalColor: String,
    val forcedView: Int,
    val buttonSpinWheelColor: String,
    val buttonSettings: ButtonSettings
)
