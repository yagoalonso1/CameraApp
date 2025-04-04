package com.example.permissionsexample.composable.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.permissionsexample.model.User

@Composable
fun UserDetailComposable(
    userData: User?,
    userName: String,
    userAge: String,
    onUserNameChange: (String) -> Unit,
    onUserAgeChange: (String) -> Unit,
    onDeleteUser: () -> Unit,
    onEditUser: () -> Unit
) {
    if (userData != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Button(
                onClick = onDeleteUser,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
            }
            
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    Text("Detalles del Usuario", fontWeight = FontWeight.Bold)
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                item {
                    TextField(
                        value = userName, 
                        onValueChange = onUserNameChange,
                        label = { Text("Nombre de usuario") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                item {
                    TextField(
                        value = userAge, 
                        onValueChange = onUserAgeChange,
                        label = { Text("Edad") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                item {
                    Text("Foto de perfil: ${userData.profilePicture}")
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                item {
                    Button(
                        onClick = onEditUser,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Guardar cambios")
                    }
                }
            }
        }
    }
} 