package ru.get4click.sdk.api

interface ResultListener {
    fun onSuccess() { }
    fun onFailure(e: Throwable) { }
}
