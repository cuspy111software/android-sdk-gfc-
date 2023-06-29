package ru.get4click.sdk.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.ContextCompat

/**
 * Convenient method to save a text to the clipboard
 *
 * @return whether operation succeed
 */
internal fun Context.copyTextToClipboard(text: String): Boolean {
    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    clipboard?.setPrimaryClip(ClipData.newPlainText(text, text))
    return clipboard != null
}
