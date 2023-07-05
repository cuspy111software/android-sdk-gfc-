package ru.get4click.sdk.api

import ru.get4click.sdk.models.CrossMailStatus

/**
 * Used to send user status
 */
interface CrossMail {
    /**
     * Sends status [Int] of an user with defined [email]
     */
    fun sendStatus(email: String, status: CrossMailStatus, resultListener: ResultListener)

    /**
     * Sends status of type [Integer] of an user with defined [email].
     *
     * **Note: this version might be used in case enum [Int] does not offer
     * appropriate status constants**
     */
    fun sendStatus(email: String, status: Int, resultListener: ResultListener)
}
