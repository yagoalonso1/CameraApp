package com.example.permissionsexample.view.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.example.permissionsexample.composable.user.UserDetailComposable
import com.example.permissionsexample.nav.Routes
import com.example.permissionsexample.viewmodel.MyViewModel

@Composable
fun UserDetailScreen(navigationController: NavHostController, myViewModel: MyViewModel) {
    // Observar datos del ViewModel
    val userData by myViewModel.actualUser.observeAsState()
    val userName by myViewModel.userName.observeAsState("")
    val userAge by myViewModel.age.observeAsState("")
    
    // Callbacks para acciones de la UI
    val onUserNameChange = { newName: String ->
        myViewModel.setUserName(newName)
    }
    
    val onUserAgeChange = { newAge: String ->
        myViewModel.setAge(newAge)
    }
    
    val onDeleteUser = {
        userData?.userId?.let { id ->
            myViewModel.deleteUser(id)
            navigationController.popBackStack()
        }
    }
    
    val onEditUser = {
        myViewModel.editUser()
        navigationController.navigate(Routes.CameraScreen.route)
    }
    
    // Mostrar el composable directamente
    UserDetailComposable(
        userData = userData,
        userName = userName ?: "",
        userAge = userAge ?: "",
        onUserNameChange = onUserNameChange,
        onUserAgeChange = onUserAgeChange,
        onDeleteUser = onDeleteUser,
        onEditUser = onEditUser
    )
} 