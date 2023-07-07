package ru.get4click.sdk.utils

import android.util.Patterns

fun String.isEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches() && this.isNotEmpty()
}