package com.example.permissionsexample.composable.camera

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.permissionsexample.composable.common.PermissionDeclinedComposable
import com.example.permissionsexample.model.User
import com.example.permissionsexample.nav.Routes
import com.example.permissionsexample.viewmodel.MyViewModel

@Composable
fun CameraScreenComposable(
    isCameraPermissionGranted: Boolean,
    showPermissionDenied: Boolean,
    bitmap: Bitmap?,
    uri: Uri?,
    permissionLauncher: ActivityResultLauncher<String>,
    navigationController: NavHostController,
    myViewModel: MyViewModel,
    userName: String,
    userAge: String,
    showRow: Boolean,
    userList: List<User>
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            TextField(
                value = userName,
                onValueChange = { myViewModel.setUserName(it) },
                label = { Text("Nombre de usuario...") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        item {
            TextField(
                value = userAge,
                onValueChange = { myViewModel.setAge(it) },
                label = { Text("Edad...") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        item {
            Button(
                onClick = {
                    if (!isCameraPermissionGranted) {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    } else {
                        navigationController.navigate(Routes.TakePhotoScreen.route)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Tomar foto")
            }
        }
        
        item {
            Button(
                onClick = {
                    navigationController.navigate(Routes.GalleryScreen.route)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Abrir galería")
            }
        }
        
        item {
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(250.dp)
                        .background(Color.Blue)
                        .border(width = 1.dp, color = Color.White, shape = CircleShape)
                )
            }
        }
        
        item {
            Button(
                onClick = {
                    myViewModel.saveUser(userName, userAge, uri)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Guardar datos")
            }
        }
        
        item {
            Button(
                onClick = {
                    myViewModel.setShowRow(true)
                    myViewModel.getUsers()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Obtener usuarios")
            }
        }
        
        if (showRow) {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(userList) { user ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.Gray),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.33f)
                                .clickable {
                                    myViewModel.getUser(user.userId!!)
                                    navigationController.navigate(Routes.UserDetailScreen.route)
                                }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(text = user.userName)
                                Text(text = user.age.toString())
                            }
                        }
                    }
                }
            }
        }
    }
    
    if (showPermissionDenied) {
        Box(modifier = Modifier.fillMaxSize()) {
            PermissionDeclinedComposable()
        }
    }
} 