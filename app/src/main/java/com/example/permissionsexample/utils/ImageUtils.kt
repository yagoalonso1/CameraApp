package com.example.permissionsexample.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log

object ImageUtils {
    /**
     * Obtiene un Bitmap a partir de una Uri
     */
    fun getImageFromUri(context: Context, uri: Uri?): Bitmap? {
        return try {
            uri?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                        decoder.isMutableRequired = true
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ImageUtils", "Error cargando imagen: ${e.message}")
            null
        }
    }
} 