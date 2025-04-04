package com.example.permissionsexample.view.common

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.permissionsexample.nav.Routes
import com.example.permissionsexample.view.camera.CameraScreen
import com.example.permissionsexample.view.camera.TakePhotoScreen
import com.example.permissionsexample.view.gallery.GalleryScreen
import com.example.permissionsexample.view.user.UserDetailScreen
import com.example.permissionsexample.viewmodel.MyViewModel

@Composable
fun MyNavHostScreen(myViewModel: MyViewModel, navigationController: NavHostController) {
    NavHost(
        navController = navigationController,
        startDestination = Routes.CameraScreen.route
    ) {
        composable(Routes.CameraScreen.route) { 
            CameraScreen(navigationController, myViewModel) 
        }
        
        composable(Routes.TakePhotoScreen.route) {
            TakePhotoScreen(navigationController, myViewModel)
        }
        
        composable(Routes.UserDetailScreen.route) {
            UserDetailScreen(navigationController, myViewModel)
        }
        
        composable(Routes.GalleryScreen.route) {
            GalleryScreen(navigationController, myViewModel)
        }
        
        composable(Routes.PermissionDeclinedScreen.route) {
            PermissionDeclinedScreen(navigationController)
        }
    }
} 