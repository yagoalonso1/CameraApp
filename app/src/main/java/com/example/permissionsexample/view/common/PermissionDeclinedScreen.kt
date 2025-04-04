package com.example.permissionsexample.view.common

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.permissionsexample.composable.common.PermissionDeclinedComposable

@Composable
fun PermissionDeclinedScreen(navigationController: NavHostController) {
    // Mostrar el composable directamente
    PermissionDeclinedComposable()
} 