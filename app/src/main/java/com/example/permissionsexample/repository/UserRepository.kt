package com.example.permissionsexample.repository

import com.example.permissionsexample.model.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val database = FirebaseFirestore.getInstance()

    fun addUser(user: User){
        database.collection("users")
            .add(
                hashMapOf(
                    "userName" to user.userName,
                    "age" to user.age,
                    "profilePicture" to user.profilePicture
                ))
    }

    fun getUsers(): CollectionReference {
        return database.collection("users")
    }

    fun getUser(userId: String): DocumentReference {
        return database.collection("users").document(userId)
    }

    fun deleteUser(userId: String) {
        database.collection("users").document(userId).delete()
    }

    fun editUser(editedUser: User) {
        database.collection("users").document(editedUser.userId!!).set(
            hashMapOf(
                "userName" to editedUser.userName,
                "age" to editedUser.age,
                "profilePicture" to editedUser.profilePicture
            )
        )
    }

}