package com.example.permissionsexample.view.camera

import android.net.Uri
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.permissionsexample.composable.camera.TakePhotoComposable
import com.example.permissionsexample.nav.Routes
import com.example.permissionsexample.viewmodel.MyViewModel

@Composable
fun TakePhotoScreen(navigationController: NavHostController, myViewModel: MyViewModel) {
    val context = LocalContext.current
    
    // Configurar la cámara
    val controller = remember {
        LifecycleCameraController(context).apply {
            CameraController.IMAGE_CAPTURE
        }
    }
    
    // Observar estados del ViewModel
    val capturedBitmap by myViewModel.capturedBitmap.observeAsState()
    val savedImageUri by myViewModel.savedImageUri.observeAsState()
    val gallerySavedSuccess by myViewModel.gallerySavedSuccess.observeAsState(false)
    
    // Estados locales de la UI
    var isPhotoTaken by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    
    // Efectos cuando se guarda la imagen en la galería
    LaunchedEffect(gallerySavedSuccess) {
        if (gallerySavedSuccess) {
            Toast.makeText(context, "Imagen guardada en la galería", Toast.LENGTH_SHORT).show()
            isSaving = false
        }
    }
    
    // Callbacks de acciones
    val onPhotoTaken = {
        myViewModel.takePhoto(context, controller) { 
            isPhotoTaken = true
        }
    }
    
    val onDiscardPhoto = {
        isPhotoTaken = false
        myViewModel.setCapturedBitmap(null)
    }
    
    val onSavePhoto = {
        capturedBitmap?.let { bitmap ->
            val uri = myViewModel.saveImageToGallery(context, bitmap)
            if (uri != null) {
                myViewModel.setCapturedImageUri(uri)
            }
        }
    }
    
    val onUsePhoto = { uri: Uri ->
        myViewModel.setCapturedImageUri(uri)
        navigationController.navigate(Routes.CameraScreen.route) {
            popUpTo(Routes.CameraScreen.route) { inclusive = true }
        }
    }
    
    val onSavingStart = {
        isSaving = true
    }
    
    val onNavigateToGallery = {
        navigationController.navigate(Routes.GalleryScreen.route)
    }
    
    val onNavigateBack = {
        navigationController.popBackStack()
    }
    
    val onSwitchCamera = {
        controller.cameraSelector =
            if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
    }
    
    // Mostrar el composable directamente
    TakePhotoComposable(
        controller = controller,
        capturedBitmap = capturedBitmap,
        savedImageUri = savedImageUri,
        isPhotoTaken = isPhotoTaken,
        isSaving = isSaving,
        onPhotoTaken = onPhotoTaken,
        onDiscardPhoto = onDiscardPhoto,
        onSavePhoto = onSavePhoto,
        onUsePhoto = onUsePhoto,
        onSavingStart = onSavingStart,
        onNavigateToGallery = onNavigateToGallery,
        onNavigateBack = onNavigateBack,
        onSwitchCamera = onSwitchCamera,
        context = context
    )
} 