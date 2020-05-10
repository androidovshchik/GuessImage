package com.mygdx.guessimage

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

private const val MAX_SIZE = 2048

val randomName: String
    get() = "${UUID.randomUUID()}.jpg"

@Suppress("MemberVisibilityCanBePrivate")
class FileManager(context: Context) {

    val externalDir: File? = context.getExternalFilesDir(null)?.apply { mkdirs() }

    val internalDir: File = context.filesDir

    val imagesDir: File
        get() = File(externalDir ?: internalDir, "images").apply { mkdirs() }

    val iconsDir: File
        get() = File(externalDir ?: internalDir, "icons").apply { mkdirs() }
}

fun copyImage(src: String, dist: String) {
    copyImage(File(src), File(dist))
}

/**
 * @return new path of file
 */
fun copyImage(src: File, dist: File) {
    if (!src.exists()) {
        return
    }
    readBitmap(src)?.use {
        writeFile(dist) {
            compress(Bitmap.CompressFormat.JPEG, 75, it)
        }
    }
}

fun readBitmap(path: String): Bitmap? {
    return readBitmap(File(path))
}

fun readBitmap(file: File): Bitmap? {
    if (!file.exists()) {
        return null
    }
    try {
        val bitmap = BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.path, this)
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
            val newWidth = if (ratio < 1) MAX_SIZE * ratio else MAX_SIZE.toFloat()
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
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }
        return newBitmap
    } catch (e: Throwable) {
        Timber.e(e)
        return null
    }
}

fun deleteFile(path: String) {
    deleteFile(File(path))
}

fun deleteFile(file: File?) {
    try {
        if (file?.isFile == true) {
            file.delete()
        }
    } catch (e: Throwable) {
        Timber.e(e)
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