package com.example.permissionsexample.view

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.permissionsexample.nav.Routes
import com.example.permissionsexample.viewmodel.MyViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CameraScreenBody(
    isCameraPermissionGranted: Boolean,
    showPermissionDenied: Boolean,
    bitmap: Bitmap?,
    uri: Uri?,
    launcher: ActivityResultLauncher<String>,
    launchImage: ActivityResultLauncher<String>,
    navigationController: NavHostController,
    myViewModel: MyViewModel
) {
    val userName by myViewModel.userName.observeAsState("")
    val userAge by myViewModel.age.observeAsState("")
    val userPicture by myViewModel.profilePicture.observeAsState("")
    val showRow by myViewModel.showRow.observeAsState(false)
    val userList by myViewModel.userList.observeAsState(emptyList())

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = userName,
            onValueChange = { myViewModel.setUserName(it) },
            label = { Text("User name...") })
        TextField(
            value = userAge,
            onValueChange = { myViewModel.setAge(it) },
            label = { Text("User age...") }
        )
        Button(onClick = {
            if (!isCameraPermissionGranted) {
                launcher.launch(Manifest.permission.CAMERA)
            } else {
                navigationController.navigate(Routes.TakePhotoScreen.route)
            }
        }) {
            Text(text = "Take photo")
        }
        Button(onClick = {
            launchImage.launch("image/*")
        }) {
            Text(text = "Open gallery")
        }
        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(250.dp)
                .background(Color.Blue)
                .border(width = 1.dp, color = Color.White, shape = CircleShape)
        )
        Button(onClick = {
            myViewModel.saveUser(userName, userAge, uri)
        }) {
            Text(text = "Save data")
        }
        /*Button(onClick = {
            Log.i("IMAGE UPLOAD", "Image uri: $uri")
            if (uri != null) myViewModel.uploadImage(uri)
        }) {
            Text(text = "Upload image")
        }*/
        Button(onClick = {
            myViewModel.setShowRow(true)
            myViewModel.getUsers()
        }) {
            Text(text = "Get users")
        }
        if (showRow) {
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
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = user.userName)
                            Text(text = user.age.toString())
                        }
                    }
                }
            }
        }
        /*GlideImage(
            model = "https://firebasestorage.googleapis.com/v0/b/prueba-456b8.appspot.com/o/images%2F2024_03_29_19_30_48?alt=media&token=26040ddc-1b60-4365-94db-0eeed2b05e10",
            contentDescription = "Character Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(100.dp)
        )*/
    }
    if (showPermissionDenied) {
        PermissionDeclinedScreen()
    }

}