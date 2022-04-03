package com.etsisi.appquitectura.data.model.dto

import com.etsisi.appquitectura.domain.model.UserBO
import com.etsisi.appquitectura.presentation.utils.EMPTY

data class UserDTO (
        val email: String = String.EMPTY,
        val id: String = String.EMPTY,
        val name: String = String.EMPTY
): FirestoreDTO() {
    fun toDomain() = UserBO(
            email = email,
            id = id,
            name = name
    )
}