package com.example.permissionsexample.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import com.example.permissionsexample.R
import com.example.permissionsexample.viewmodel.MyViewModel

@Composable
fun CameraScreen(navigationController: NavHostController, myViewModel: MyViewModel) {
    val context = LocalContext.current
    val img: Bitmap? = ContextCompat.getDrawable(context, R.drawable.empty_image)?.toBitmap()
    var bitmap by remember { mutableStateOf(img) }
    var uri by remember { mutableStateOf<Uri?>(null) }
    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            Log.i("IMAGE UPLOAD", "Image uri: $it")
            bitmap = getImage(context, it)
            uri = it
        })
    val isCameraPermissionGranted by myViewModel.cameraPermissionGranted.observeAsState(false)
    val shouldShowPermissionRationale by myViewModel.shouldShowPermissionRationale.observeAsState(
        false
    )
    val showPermissionDenied by myViewModel.showPermissionDenied.observeAsState(false)
    val launcher = MyManagedActivityResultLauncher(myViewModel, context, shouldShowPermissionRationale)

    CameraScreenBody(
        isCameraPermissionGranted,
        showPermissionDenied,
        bitmap,
        uri,
        launcher,
        launchImage,
        navigationController, myViewModel
    )
}