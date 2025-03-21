package com.example.permissionsexample.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.permissionsexample.model.User
import com.example.permissionsexample.repository.UserRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyViewModel : ViewModel() {
    private val _cameraPermissionGranted = MutableLiveData(false)
    val cameraPermissionGranted = _cameraPermissionGranted

    private val _shouldShowPermissionRationale = MutableLiveData(false)
    val shouldShowPermissionRationale = _shouldShowPermissionRationale

    private val _showPermissionDenied = MutableLiveData(false)
    val showPermissionDenied = _showPermissionDenied

    fun setCameraPermissionGranted(granted: Boolean) {
        _cameraPermissionGranted.value = granted
    }

    fun setShouldShowPermissionRationale(should: Boolean) {
        _shouldShowPermissionRationale.value = should
    }

    fun setShowPermissionDenied(denied: Boolean) {
        _showPermissionDenied.value = denied
    }

    private val repository = UserRepository()

    private val _userName = MutableLiveData("")
    val userName = _userName
    private val _age = MutableLiveData("")
    val age = _age
    private val _profilePicture = MutableLiveData("")
    val profilePicture = _profilePicture
    private val _showRow = MutableLiveData(false)
    val showRow = _showRow
    private val _userList = MutableLiveData(emptyList<User>())
    val userList = _userList
    private val _actualUser = MutableLiveData<User?>()
    val actualUser = _actualUser

    fun setShowRow(show: Boolean) {
        _showRow.value = show
    }

    fun setUserName(name: String) {
        _userName.value = name
    }

    fun setAge(age: String) {
        _age.value = age
    }

    fun setProfilePicture(picture: String) {
        _profilePicture.value = picture
    }

    fun saveUser(userName: String?, userAge: String?, userPicture: Uri?) {
        if(userPicture != null) {
            uploadImage(userPicture, userName, userAge)
        }
    }

    fun getUsers() {
        repository.getUsers().addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore error", error.message.toString())
                return@addSnapshotListener
            }
            val tempList = mutableListOf<User>()
            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    val newUser = dc.document.toObject(User::class.java)
                    newUser.userId = dc.document.id
                    tempList.add(newUser)
                }
            }
            _userList.value = tempList
        }
    }

    fun getUser(userId: String) {
        repository.getUser(userId).addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("UserRepository", "Listen failed.", error)
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {
                val user = value.toObject(User::class.java)
                if (user != null) {
                    user.userId = userId
                }
                _actualUser.value = user
                _userName.value = _actualUser.value!!.userName
                _age.value = _actualUser.value!!.age.toString()

            } else {
                Log.d("UserRepository", "Current data: null")
            }
        }
    }

    fun deleteUser(userId: String) {
        repository.deleteUser(userId)
    }

    fun editUser() {
        val editedUser = User(
            actualUser.value!!.userId,
            userName.value!!,
            age.value!!.toInt(),
            profilePicture.value
        )
        repository.editUser(editedUser)
    }

    fun uploadImage(imageUri: Uri, userName: String?, userAge: String?) {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("images/$fileName")
        storage.putFile(imageUri)
            .addOnSuccessListener {
                Log.i("IMAGE UPLOAD", "Image uploaded successfully")
                storage.downloadUrl.addOnSuccessListener {
                    Log.i("IMAGEN", it.toString())
                    repository.addUser(User(null, userName!!, userAge!!.toInt(), it.toString()))
                }

            }
            .addOnFailureListener {
                Log.i("IMAGE UPLOAD", "Image upload failed")
            }
    }

    fun takePhoto(
        context: Context,
        controller: LifecycleCameraController,
        onPhotoTaken: (Bitmap) -> Unit
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    //Para que la foto no salga rotada
                    /*val matri = Matrix().apply{
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matri,
                        true
                    )
                    onPhotoTaken(rotatedBitmap)*/
                    onPhotoTaken(image.toBitmap())
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Error taken photo", exception)
                }
            }
        )
    }

}