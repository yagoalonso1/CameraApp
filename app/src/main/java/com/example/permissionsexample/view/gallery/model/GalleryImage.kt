package com.example.permissionsexample.view.gallery.model

import android.graphics.Bitmap
import android.net.Uri

data class GalleryImage(
    val id: Long,
    val uri: Uri,
    val name: String,
    val bitmap: Bitmap? = null
) 