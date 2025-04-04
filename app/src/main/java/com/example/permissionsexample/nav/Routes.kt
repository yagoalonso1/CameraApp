package com.example.permissionsexample.nav

sealed class Routes(val route: String) {
    object CameraScreen : Routes("cameraScreen")
    object TakePhotoScreen : Routes("takePhotoScreen")
    object UserDetailScreen : Routes("userDetailScreen")
    object GalleryScreen : Routes("galleryScreen")
    object PermissionDeclinedScreen : Routes("permissionDeclinedScreen")
}