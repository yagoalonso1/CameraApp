package com.example.permissionsexample.view.gallery

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.permissionsexample.composable.gallery.GalleryComposable
import com.example.permissionsexample.nav.Routes
import com.example.permissionsexample.view.gallery.model.GalleryImage
import com.example.permissionsexample.viewmodel.MyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun GalleryScreen(navigationController: NavHostController, viewModel: MyViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Estados de la UI
    val images = remember { mutableStateListOf<GalleryImage>() }
    var isLoading by remember { mutableStateOf(true) }
    var selectedImage by remember { mutableStateOf<GalleryImage?>(null) }
    
    // Configuración del launcher de permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                scope.launch {
                    isLoading = true
                    loadImagesFromGallery(context, images)
                    isLoading = false
                }
            } else {
                Toast.makeText(context, "Se necesitan permisos para acceder a la galería", Toast.LENGTH_LONG).show()
                navigationController.popBackStack()
            }
        }
    )
    
    // Solicitar permisos al iniciar la pantalla
    LaunchedEffect(key1 = true) {
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    
    // Callbacks para acciones de la UI
    val onBackClick = {
        navigationController.popBackStack()
    }
    
    val onImageSelected = { image: GalleryImage ->
        selectedImage = image
    }
    
    val onImageUse = {
        selectedImage?.let { image ->
            viewModel.setCapturedImageUri(image.uri)
            navigationController.navigate(Routes.CameraScreen.route) {
                popUpTo(Routes.CameraScreen.route) { inclusive = true }
            }
        }
    }
    
    val onReturnToGallery = {
        selectedImage = null
    }
    
    // Mostrar el composable directamente
    GalleryComposable(
        images = images,
        isLoading = isLoading,
        selectedImage = selectedImage,
        onBackClick = onBackClick,
        onImageSelected = onImageSelected,
        onImageUse = onImageUse,
        onReturnToGallery = onReturnToGallery
    )
}

suspend fun loadImagesFromGallery(context: Context, images: MutableList<GalleryImage>) {
    withContext(Dispatchers.IO) {
        try {
            val collection = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME
            )
            
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
            
            context.contentResolver.query(
                collection,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                
                images.clear()
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    
                    // Cargar miniatura
                    val thumbBitmap = try {
                        withContext(Dispatchers.IO) {
                            val options = BitmapFactory.Options().apply {
                                inSampleSize = 8 // Reducir tamaño para miniatura
                            }
                            val inputStream = context.contentResolver.openInputStream(contentUri)
                            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
                            inputStream?.close()
                            bitmap
                        }
                    } catch (e: Exception) {
                        Log.e("GalleryScreen", "Error cargando miniatura: ${e.message}")
                        null
                    }
                    
                    images.add(GalleryImage(id, contentUri, name, thumbBitmap))
                    
                    // Limitar el número de imágenes para el rendimiento
                    if (images.size >= 100) break
                }
            }
        } catch (e: Exception) {
            Log.e("GalleryScreen", "Error cargando imágenes: ${e.message}")
        }
    }
} 