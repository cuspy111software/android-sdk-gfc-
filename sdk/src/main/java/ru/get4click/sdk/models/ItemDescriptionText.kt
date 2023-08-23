package ru.get4click.sdk.models

enum class ItemDescriptionTextStyle {
    Small,
    Big
}

data class ItemDescriptionText(
    val text: String,
    val style: ItemDescriptionTextStyle
)
