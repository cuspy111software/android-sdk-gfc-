package ru.get4click.sdk.data.utlis

import org.json.JSONObject

internal fun JSONObject.isStatusOk(): Boolean {
    return getString("status").equals("OK", ignoreCase = false)
}
