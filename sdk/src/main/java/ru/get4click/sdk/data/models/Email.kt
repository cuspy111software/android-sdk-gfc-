package ru.get4click.sdk.data.models

@JvmInline
internal value class Email(val value: String) {
    init {
        android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches() && value.isNotEmpty()
    }
}
