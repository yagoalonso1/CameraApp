package com.example.permissionsexample.viewmodel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.permissionsexample.model.DBHelper
import com.example.permissionsexample.model.User
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyViewModel : ViewModel() {
    // Estado de los permisos
    private val _cameraPermissionGranted = MutableLiveData<Boolean>()
    val cameraPermissionGranted: LiveData<Boolean> = _cameraPermissionGranted

    private val _shouldShowPermissionRationale = MutableLiveData<Boolean>()
    val shouldShowPermissionRationale: LiveData<Boolean> = _shouldShowPermissionRationale

    private val _showPermissionDenied = MutableLiveData<Boolean>()
    val showPermissionDenied: LiveData<Boolean> = _showPermissionDenied

    // Estado de la imagen capturada
    private val _capturedBitmap = MutableLiveData<Bitmap?>()
    val capturedBitmap: LiveData<Bitmap?> = _capturedBitmap
    
    private val _capturedImageUri = MutableLiveData<Uri?>()
    val capturedImageUri: LiveData<Uri?> = _capturedImageUri
    
    private val _currentBitmapImage = MutableLiveData<Bitmap?>()
    val currentBitmapImage: LiveData<Bitmap?> = _currentBitmapImage
    
    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> = _currentImageUri
    
    // Estado de guardado en galería
    private val _savedImageUri = MutableLiveData<Uri?>()
    val savedImageUri: LiveData<Uri?> = _savedImageUri
    
    private val _gallerySavedSuccess = MutableLiveData<Boolean>()
    val gallerySavedSuccess: LiveData<Boolean> = _gallerySavedSuccess

    // Datos de usuario
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _age = MutableLiveData<String>()
    val age: LiveData<String> = _age

    private val _userList = MutableLiveData<MutableList<User>>()
    val userList: LiveData<MutableList<User>> = _userList

    private val _showRow = MutableLiveData<Boolean>()
    val showRow: LiveData<Boolean> = _showRow

    private val _actualUser = MutableLiveData<User?>()
    val actualUser: LiveData<User?> = _actualUser

    // Métodos para estado de permisos
    fun setCameraPermissionGranted(granted: Boolean) {
        _cameraPermissionGranted.value = granted
    }

    fun setShouldShowPermissionRationale(shouldShow: Boolean) {
        _shouldShowPermissionRationale.value = shouldShow
    }

    fun setShowPermissionDenied(show: Boolean) {
        _showPermissionDenied.value = show
    }
    
    // Métodos para la imagen actual
    fun setCurrentBitmapImage(bitmap: Bitmap?) {
        _currentBitmapImage.value = bitmap
    }
    
    fun setCurrentImageUri(uri: Uri?) {
        _currentImageUri.value = uri
    }

    // Métodos para captura de imagen
    fun setCapturedBitmap(bitmap: Bitmap?) {
        _capturedBitmap.value = bitmap
    }
    
    fun setCapturedImageUri(uri: Uri?) {
        _capturedImageUri.value = uri
    }
    
    fun setSavedImageUri(uri: Uri?) {
        _savedImageUri.value = uri
    }
    
    fun setGallerySavedSuccess(success: Boolean) {
        _gallerySavedSuccess.value = success
    }

    // Métodos para datos de usuario
    fun setUserName(name: String) {
        _userName.value = name
    }

    fun setAge(age: String) {
        _age.value = age
    }

    fun setShowRow(show: Boolean) {
        _showRow.value = show
    }

    fun setActualUser(user: User?) {
        _actualUser.value = user
        _userName.value = user?.userName ?: ""
        _age.value = user?.age.toString()
    }

    // Métodos para tomar foto y guardar en galería
    fun takePhoto(context: Context, controller: LifecycleCameraController, onPhotoTaken: (Bitmap) -> Unit) {
        controller.takePicture(context.mainExecutor, { capturedImage ->
            Log.i("TakePhotoScreen", "Foto capturada correctamente")
            _capturedBitmap.value = capturedImage.toBitmap()
            onPhotoTaken(capturedImage.toBitmap())
        })
    }

    fun saveImageToGallery(context: Context, bitmap: Bitmap): Uri? {
        var uri: Uri? = null
        try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageName = "IMG_$timestamp.jpg"
            
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }
            
            val resolver = context.contentResolver
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                uri?.let { imageUri ->
                    val outputStream = resolver.openOutputStream(imageUri)
                    outputStream?.use { output ->
                        saveBitmapToOutputStream(bitmap, output)
                    }
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(imageUri, contentValues, null, null)
                }
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, imageName)
                val outputStream = FileOutputStream(image)
                outputStream.use { output ->
                    saveBitmapToOutputStream(bitmap, output)
                }
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DATA, image.absolutePath)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }
                uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            }
            
            // Actualizar estado
            _savedImageUri.value = uri
            _gallerySavedSuccess.value = true
            
            return uri
        } catch (e: Exception) {
            Log.e("MyViewModel", "Error guardando imagen: ${e.message}")
            _gallerySavedSuccess.value = false
            return null
        }
    }
    
    private fun saveBitmapToOutputStream(bitmap: Bitmap, outputStream: OutputStream) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    }

    // Métodos para base de datos
    fun saveUser(userName: String, age: String, uri: Uri?) {
        viewModelScope.launch {
            try {
                val user = User(null, userName, age.toIntOrNull() ?: 0, uri?.toString())
                val dbHelper = DBHelper.getInstance()
                dbHelper.insertUser(user)
            } catch (e: Exception) {
                Log.e("MyViewModel", "Error guardando usuario: ${e.message}")
            }
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            try {
                val dbHelper = DBHelper.getInstance()
                val users = dbHelper.getUsers()
                _userList.value = users
            } catch (e: Exception) {
                Log.e("MyViewModel", "Error obteniendo usuarios: ${e.message}")
            }
        }
    }

    fun getUser(userId: Long) {
        viewModelScope.launch {
            try {
                val dbHelper = DBHelper.getInstance()
                val user = dbHelper.getUser(userId)
                setActualUser(user)
            } catch (e: Exception) {
                Log.e("MyViewModel", "Error obteniendo usuario: ${e.message}")
            }
        }
    }

    fun editUser() {
        viewModelScope.launch {
            try {
                val dbHelper = DBHelper.getInstance()
                val user = User(
                    _actualUser.value?.userId,
                    _userName.value ?: "",
                    _age.value?.toIntOrNull() ?: 0,
                    _capturedImageUri.value?.toString() ?: _actualUser.value?.profilePicture
                )
                
                if (user.userId != null) {
                    dbHelper.updateUser(user)
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Error actualizando usuario: ${e.message}")
            }
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            try {
                val dbHelper = DBHelper.getInstance()
                dbHelper.deleteUser(userId)
            } catch (e: Exception) {
                Log.e("MyViewModel", "Error eliminando usuario: ${e.message}")
            }
        }
    }
}