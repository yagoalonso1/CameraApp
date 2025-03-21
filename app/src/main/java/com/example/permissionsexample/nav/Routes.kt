package com.example.permissionsexample.nav

sealed class Routes(val route: String) {
    object CameraScreen: Routes("cameraScreen")
    object TakePhotoScreen: Routes("photoScreen")
    object UserDetailScreen: Routes("userDetailScreen")
}