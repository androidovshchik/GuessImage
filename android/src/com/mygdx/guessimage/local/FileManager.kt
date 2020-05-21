@file:Suppress("unused")

package com.mygdx.guessimage.local

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import com.mygdx.guessimage.extension.use
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.math.max
import kotlin.math.min

private const val MIN_SIZE = 256

private const val MAX_SIZE = 2048

private const val THUMB_SIZE = 512

private const val QUALITY = 80

val File.randomName: String
    get() = "${UUID.randomUUID()}.$extension"

@Suppress("MemberVisibilityCanBePrivate")
class FileManager(context: Context) {

    val externalDir: File? = context.getExternalFilesDir(null)?.apply { mkdirs() }

    val internalDir: File = context.filesDir

    val imagesDir: File
        get() = File(externalDir ?: internalDir, "images").apply { mkdirs() }

    val iconsDir: File
        get() = File(externalDir ?: internalDir, "icons").apply { mkdirs() }

    fun getImageFile(name: String?) = File(imagesDir, name.orEmpty())

    fun getIconFile(name: String?) = File(iconsDir, name.orEmpty())

    @SuppressLint("DefaultLocale")
    fun copyImage(path: String?): String? {
        if (path.isNullOrBlank()) {
            return null
        }
        val src = File(path)
        if (!src.exists()) {
            return null
        }
        return createCopy(src)?.use {
            val filename = src.randomName
            val format = when (src.extension.toLowerCase()) {
                "png" -> Bitmap.CompressFormat.PNG
                "webp" -> Bitmap.CompressFormat.WEBP
                else -> Bitmap.CompressFormat.JPEG
            }
            writeFile(File(imagesDir, filename)) {
                compress(format, QUALITY, it)
            }
            createThumb()?.use {
                writeFile(File(iconsDir, filename)) {
                    compress(format, QUALITY, it)
                }
            }
            filename
        }
    }
}

fun deleteFile(file: File?) {
    try {
        if (file?.isFile == true) {
            file.delete()
        }
    } catch (ignored: Throwable) {
    }
}

inline fun writeFile(dist: File, block: (FileOutputStream) -> Unit): Boolean {
    return try {
        FileOutputStream(dist).use { output ->
            block(output)
            output.flush()
        }
        true
    } catch (e: Throwable) {
        Timber.e(e)
        deleteFile(dist)
        false
    }
}

private fun createCopy(file: File): Bitmap? {
    if (!file.exists()) {
        return null
    }
    try {
        val bitmap = BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.path, this)
            if (outHeight < MIN_SIZE || outWidth < MIN_SIZE) {
                return null
            }
            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(MAX_SIZE, MAX_SIZE)
            inJustDecodeBounds = false
            // Decode bitmap with inSampleSize set
            BitmapFactory.decodeFile(file.path, this)
        }
        val matrix = Matrix()
        val width = bitmap.width
        val height = bitmap.height
        if (width > MAX_SIZE || height > MAX_SIZE) {
            val ratio = width.toFloat() / height
            val newWidth = MAX_SIZE * min(1f, ratio)
            val scale = newWidth / width
            matrix.preScale(scale, scale)
        }
        val exif = ExifInterface(file)
        val rotation = when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
        if (rotation % 360 != 0) {
            matrix.postRotate(rotation.toFloat())
        }
        val newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        if (newBitmap != bitmap) {
            try {
                bitmap.recycle()
            } catch (ignored: Throwable) {
            }
        }
        return newBitmap
    } catch (e: Throwable) {
        Timber.e(e)
        return null
    }
}

private fun Bitmap.createThumb(): Bitmap? {
    return try {
        val matrix = Matrix()
        val maxSize = max(width, height)
        if (maxSize > THUMB_SIZE) {
            val scale = THUMB_SIZE.toFloat() / maxSize
            matrix.preScale(scale, scale)
        }
        val x = max(0, (width - THUMB_SIZE) / 2)
        val y = max(0, (height - THUMB_SIZE) / 2)
        val width = min(THUMB_SIZE, width)
        val height = min(THUMB_SIZE, height)
        Bitmap.createBitmap(this, x, y, width, height, matrix, true)
    } catch (e: Throwable) {
        Timber.e(e)
        null
    }
}

private fun BitmapFactory.Options.calculateInSampleSize(reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = outHeight to outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2
        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}