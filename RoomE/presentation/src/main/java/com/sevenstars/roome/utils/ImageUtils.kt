package com.sevenstars.roome.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.provider.MediaStore
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

object ImageUtils {

    fun saveViewToGallery(context: Context, view: View): Boolean {
        val bitmap = getBitmapFromView(view)
        val contentResolver = context.contentResolver

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "profile_image_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/")
                put(MediaStore.Images.Media.IS_PENDING, true)
            }
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        return if (uri != null) {
            try {
                val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
                outputStream?.use {
                    if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)) {
                        throw IOException("Failed to save bitmap.")
                    }
                    it.flush()
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, false)
                    contentResolver.update(uri, contentValues, null, null)
                }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        } else {
            false
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun captureViewToCache(context: Context, filename: String, view: View): File? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return saveBitmapToCache(context, bitmap, filename)
    }

    private fun saveBitmapToCache(context: Context, bitmap: Bitmap, filename: String): File? {
        var outputStream: OutputStream? = null
        return try {
            val cacheDir = File(context.cacheDir, "images")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            val file = File(cacheDir, "$filename.png")
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            outputStream?.close()
        }
    }
}
