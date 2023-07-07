package ru.get4click.sdk.data.utlis

import org.json.JSONObject
import ru.get4click.sdk.data.models.promocode.PromoCodeApiModel

internal fun JSONObject.parseToModel(): PromoCodeApiModel {
    return PromoCodeApiModel(
        couponTitle       = getString("couponTitle"),
        couponDescription = getString("couponDescription"),
        couponLimitations = getString("couponLimitations"),
        orderRestrictions = getString("orderRestrictions"),
        couponCode        = getString("couponCode")
    )
}
