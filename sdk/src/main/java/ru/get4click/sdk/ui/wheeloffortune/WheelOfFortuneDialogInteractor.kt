package ru.get4click.sdk.ui.wheeloffortune

import ru.get4click.sdk.data.models.Email
import ru.get4click.sdk.data.models.GiftId

internal interface WheelOfFortuneDialogInteractor {
    /**
     * Returns the position to spin
     */
    suspend fun spin(): Result<GiftId>

    /**
     * Send chosen gift to the user (via email for example)
     */
    suspend fun makeDistribution(giftId: GiftId, email: Email): Result<Unit>
}
