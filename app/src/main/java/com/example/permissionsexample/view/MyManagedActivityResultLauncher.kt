package com.example.permissionsexample.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.example.permissionsexample.viewmodel.MyViewModel

@Composable
fun MyManagedActivityResultLauncher(
    myViewModel: MyViewModel,
    context: Context,
    shouldShowPermissionRationale: Boolean
): ManagedActivityResultLauncher<String, Boolean> {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                myViewModel.setCameraPermissionGranted(true)
            } else {
                myViewModel.setShouldShowPermissionRationale(
                    shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.CAMERA
                    )
                )
                if (!shouldShowPermissionRationale) {
                    Log.i("CameraScreen", "No podemos volver a pedir permisos")
                    myViewModel.setShowPermissionDenied(true)
                }
            }
        }
    )
    return launcher
}

fun getImage(context: Context, uri: Uri?): Bitmap? {
    return if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    } else {
        val source = uri?.let {
            ImageDecoder.createSource(context.contentResolver, it)
        }
        source?.let {
            ImageDecoder.decodeBitmap(it)
        }!!
    }
}