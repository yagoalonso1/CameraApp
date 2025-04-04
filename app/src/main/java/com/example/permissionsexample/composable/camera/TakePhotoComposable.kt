package com.example.permissionsexample.composable.camera

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.permissionsexample.composable.common.CameraPreviewComposable

@Composable
fun TakePhotoComposable(
    controller: LifecycleCameraController,
    capturedBitmap: Bitmap?,
    savedImageUri: Uri?,
    isPhotoTaken: Boolean,
    isSaving: Boolean,
    onPhotoTaken: () -> Unit,
    onDiscardPhoto: () -> Unit,
    onSavePhoto: () -> Unit,
    onUsePhoto: (Uri) -> Unit,
    onSavingStart: () -> Unit,
    onNavigateToGallery: () -> Unit,
    onNavigateBack: () -> Unit,
    onSwitchCamera: () -> Unit,
    context: Context
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (!isPhotoTaken) {
            // Modo cámara
            CameraPreviewComposable(controller = controller, modifier = Modifier.fillMaxSize())
            
            // Botón para cambiar de cámara
            IconButton(
                onClick = onSwitchCamera,
                modifier = Modifier.offset(16.dp, 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Cambiar cámara",
                    tint = Color.White
                )
            }
            
            // Botón para volver atrás
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.offset(16.dp, 80.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            
            // Controles inferiores
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Botón para abrir galería
                        IconButton(
                            onClick = onNavigateToGallery,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Photo,
                                contentDescription = "Abrir galería",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        
                        // Botón para tomar foto
                        IconButton(
                            onClick = onPhotoTaken,
                            modifier = Modifier.size(80.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = "Tomar foto",
                                tint = Color.White,
                                modifier = Modifier.size(60.dp)
                            )
                        }
                        
                        // Espacio vacío para mantener centrado el botón de la cámara
                        Spacer(modifier = Modifier.size(64.dp))
                    }
                }
            }
        } else {
            // Modo previsualización de foto tomada
            capturedBitmap?.let { bitmap ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp)
                                .padding(bottom = 16.dp)
                        ) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Foto capturada",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Botón para descartar y volver a la cámara
                            Button(onClick = onDiscardPhoto) {
                                Text("Descartar")
                            }
                            
                            // Botón para guardar en la galería
                            Button(
                                onClick = {
                                    onSavingStart()
                                    onSavePhoto()
                                },
                                enabled = !isSaving
                            ) {
                                if (isSaving) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = Color.White
                                    )
                                } else {
                                    Text("Guardar en galería")
                                }
                            }
                        }
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    item {
                        // Botón para usar la foto y volver a la pantalla principal
                        Button(
                            onClick = { 
                                savedImageUri?.let { uri ->
                                    onUsePhoto(uri)
                                } ?: run {
                                    Toast.makeText(context, "Guarda la imagen primero", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            enabled = savedImageUri != null
                        ) {
                            Text("Usar esta foto")
                        }
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
} 