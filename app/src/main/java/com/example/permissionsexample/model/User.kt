package com.example.permissionsexample.model

data class User(
    var userId: String? = null,
    var userName: String,
    var age: Int,
    var profilePicture: String? = null
) {
    //Necessari per poder convetir les dades de firestore al nostre model User
    constructor() : this(null, "", 0, null)
}
