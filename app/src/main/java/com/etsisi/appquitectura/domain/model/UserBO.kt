package com.etsisi.appquitectura.domain.model

data class UserBO(
    val id: String,
    val name: String,
    val email: String
): FirestoreBO()
