package ru.get4click.sdk.data

import ru.get4click.sdk.data.models.Email

internal interface CrossMailApi {
    suspend fun sendEmail(apiKey: String, email: Email, status: Int): Result<Unit>
}
