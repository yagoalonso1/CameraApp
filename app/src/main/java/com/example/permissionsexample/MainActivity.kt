package com.example.permissionsexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.permissionsexample.ui.theme.PermissionsExampleTheme
import com.example.permissionsexample.view.common.MyNavHostScreen
import com.example.permissionsexample.viewmodel.MyViewModel

class MainActivity : ComponentActivity() {
    // Declarar el ViewModel a nivel de clase
    private lateinit var myViewModel: MyViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar el ViewModel usando ViewModelProvider para que sobreviva a la rotación
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]
        
        setContent {
            PermissionsExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navigationController = rememberNavController()
                    MyNavHostScreen(myViewModel, navigationController)
                }
            }
        }
    }
}
