package ru.get4click.sdk.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed class Image {
    class Svg(val path: String) : Image()
    class BitmapImg(val bitmap: Bitmap) : Image()
}

object ImageDownloader {
    suspend fun downloadInto(path: String): Image? {
        return when (path.split(".").last().lowercase()) {
            "svg" -> Image.Svg(path)
            "png", "jpg", "jpeg", "bmp", "webp" -> {
                val b = suspendCoroutine { cont ->
                    val bitmap = try {
                        val url = URL(path)
                        val connection = url.openConnection() as HttpURLConnection
                        connection.doInput = true
                        connection.connect()
                        val stream = connection.inputStream
                        BitmapFactory.decodeStream(stream)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        null
                    }
                    cont.resume(bitmap)
                }
                b?.let(Image::BitmapImg)
            }
            else -> null
        }
    }
}
