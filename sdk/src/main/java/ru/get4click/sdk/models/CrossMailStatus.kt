package ru.get4click.sdk.models

/**
 * [EnterApp] - User has enter the application
 * [Order] - User has made an order
 */
enum class CrossMailStatus(val value: Int) {
    EnterApp(0),
    Order(1)
}
