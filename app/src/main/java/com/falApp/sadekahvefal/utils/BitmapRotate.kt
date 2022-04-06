package com.falApp.sadekahvefal.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import androidx.exifinterface.media.ExifInterface
import java.io.IOException
import java.io.InputStream

/**
 * This method is responsible for solving the rotation issue if exist. Also scale the images to
 * 1024x1024 resolution
 *
 * @param context       The current context
 * @param selectedImage The Image URI
 * @return Bitmap image results
 * @throws IOException
 */

fun handleSamplingAndRotationBitmap(context: Context, selectedImage: Uri) : Bitmap {
    val MAX_HEIGHT = 1024
    val MAX_WIDTH = 1024

    // First decode with inJustDecodeBounds=true to check dimensions
    val  options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    var imageStream = context.contentResolver.openInputStream(selectedImage)
    BitmapFactory.decodeStream(imageStream, null, options)
    imageStream!!.close()

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    imageStream = context.contentResolver.openInputStream(selectedImage)
    var img = BitmapFactory.decodeStream(imageStream, null, options)
    img = rotateImageIfRequired(context, img!!, selectedImage)
    return img!!
}

private fun calculateInSampleSize(options : BitmapFactory.Options, reqWidth : Int, reqHeight : Int) : Int{
    val height = options.outHeight
    val width  = options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        // Calculate ratios of height and width to requested height and width
        val heightRatio = Math.round((height / reqHeight).toFloat())
        val widthRatio =  Math.round((width / reqWidth).toFloat())

        // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
        // with both dimensions larger than or equal to the requested height and width.
        inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio

        // This offers some additional logic in case the image has a strange
        // aspect ratio. For example, a panorama may have a much larger
        // width than height. In these cases the total pixels might still
        // end up being too large to fit comfortably in memory, so we should
        // be more aggressive with sample down the image (=larger inSampleSize).
        val totalPixels = width * height

        // Anything more than 2x the requested pixels we'll sample down further
        val totalReqPixelsCap = reqWidth * reqHeight * 2
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
    }
    return inSampleSize
}

@Throws(IOException::class)
private fun rotateImageIfRequired(
    context: Context,
    img: Bitmap,
    selectedImage: Uri
): Bitmap? {
    val input: InputStream = context.contentResolver.openInputStream(selectedImage)!!
    val ei: ExifInterface
    ei = if (Build.VERSION.SDK_INT > 23) ExifInterface(input) else ExifInterface(selectedImage.path!!)
    val orientation = ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(
            img, 90f
        )
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(
            img, 180f
        )
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(
            img, 270f
        )
        else -> img
    }
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height,
        matrix, true
    )
}

fun scaleDown(
    realImage: Bitmap, maxImageSize: Float,
    filter: Boolean
): Bitmap? {
    val ratio = Math.min(
        maxImageSize / realImage.width,
        maxImageSize / realImage.height
    )
    val width = Math.round(ratio * realImage.width)
    val height = Math.round(ratio * realImage.height)
    return Bitmap.createScaledBitmap(
        realImage, width,
        height, filter
    )
}