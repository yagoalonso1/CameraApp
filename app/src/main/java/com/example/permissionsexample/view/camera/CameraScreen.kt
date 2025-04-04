package com.example.permissionsexample.view.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.example.permissionsexample.composable.camera.CameraScreenComposable
import com.example.permissionsexample.utils.ImageUtils
import com.example.permissionsexample.view.common.MyManagedActivityResultLauncher
import com.example.permissionsexample.viewmodel.MyViewModel

@Composable
fun CameraScreen(navigationController: NavHostController, myViewModel: MyViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // Observar estados desde el ViewModel
    val capturedImageUri by myViewModel.capturedImageUri.observeAsState()
    val isCameraPermissionGranted by myViewModel.cameraPermissionGranted.observeAsState(false)
    val shouldShowPermissionRationale by myViewModel.shouldShowPermissionRationale.observeAsState(false)
    val showPermissionDenied by myViewModel.showPermissionDenied.observeAsState(false)
    val bitmap by myViewModel.currentBitmapImage.observeAsState()
    val userName by myViewModel.userName.observeAsState("")
    val userAge by myViewModel.age.observeAsState("")
    val showRow by myViewModel.showRow.observeAsState(false)
    val userList by myViewModel.userList.observeAsState(emptyList())
    
    // Observer para actualizar la imagen cuando se vuelve de la cámara
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                capturedImageUri?.let {
                    myViewModel.setCurrentImageUri(it)
                    val imageBitmap = ImageUtils.getImageFromUri(context, it)
                    myViewModel.setCurrentBitmapImage(imageBitmap)
                }
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    // Obtener el launcher de permisos
    val permissionLauncher = MyManagedActivityResultLauncher(
        myViewModel, 
        context, 
        shouldShowPermissionRationale
    )

    // Mostrar el composable directamente
    CameraScreenComposable(
        isCameraPermissionGranted = isCameraPermissionGranted,
        showPermissionDenied = showPermissionDenied,
        bitmap = bitmap,
        uri = capturedImageUri,
        permissionLauncher = permissionLauncher,
        navigationController = navigationController, 
        myViewModel = myViewModel,
        userName = userName,
        userAge = userAge,
        showRow = showRow,
        userList = userList
    )
} 