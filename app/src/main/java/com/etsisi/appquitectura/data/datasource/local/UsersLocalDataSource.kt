package com.etsisi.appquitectura.data.datasource.local

import com.etsisi.appquitectura.data.datasource.local.dao.UsersDAO
import com.etsisi.appquitectura.data.model.entities.UserEntity
import com.etsisi.appquitectura.domain.model.UserBO
import com.etsisi.appquitectura.domain.usecase.UpdateUserDetailsUseCase

class UsersLocalDataSource(
        private val dao: UsersDAO
) {

    suspend fun getUserById(id: String): UserBO? {
        return dao.getUserById(id)?.toDomain()
    }

    suspend fun addUsers(vararg users: UserBO) {
        users.map {
            dao.addUser(it.toEntity())
        }
    }

    suspend fun updateUser(user: UserEntity) {
       dao.update(user)
    }

}