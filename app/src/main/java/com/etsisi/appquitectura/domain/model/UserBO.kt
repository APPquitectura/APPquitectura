package com.etsisi.appquitectura.domain.model

import com.etsisi.appquitectura.data.model.dto.FirestoreDTO

data class UserBO(
    val id: String,
    val name: String,
    val email: String
)
