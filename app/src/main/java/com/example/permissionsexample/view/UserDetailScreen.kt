package com.example.permissionsexample.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.permissionsexample.nav.Routes
import com.example.permissionsexample.viewmodel.MyViewModel

@Composable
fun UserDetailScreen(navigationController: NavHostController, myViewModel: MyViewModel) {
    val userData by myViewModel.actualUser.observeAsState()
    val userName by myViewModel.userName.observeAsState()
    val userAge by myViewModel.age.observeAsState()
    if (userData != null) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                myViewModel.deleteUser(userData!!.userId!!)
                navigationController.popBackStack()
            }, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("User Detail Screen", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            TextField(value = userName!!, onValueChange = { myViewModel.setUserName(it) })
            TextField(value = userAge!!, onValueChange = { myViewModel.setAge(it) })
            Text("Profile Picture: ${userData?.profilePicture}")
            Button(onClick = {
                myViewModel.editUser()
                navigationController.navigate(Routes.CameraScreen.route)
            }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                Text(text = "Edit")
            }
        }
    }
}