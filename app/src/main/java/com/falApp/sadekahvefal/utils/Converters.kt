package com.falApp.sadekahvefal.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

class Converters {

    fun toBitmap(bytes : ByteArray?): Bitmap? {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
    }


    fun fromBitmap(bmp : Bitmap?): String?{
        val outputStream = ByteArrayOutputStream()
        bmp!!.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }
}