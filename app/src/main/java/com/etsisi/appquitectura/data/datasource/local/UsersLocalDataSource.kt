package com.etsisi.appquitectura.data.datasource.local

import com.etsisi.appquitectura.data.datasource.local.dao.UsersDAO
import com.etsisi.appquitectura.data.model.entities.UserEntity

class UsersLocalDataSource(
        private val dao: UsersDAO
) {

    suspend fun addAllUsers(users: List<UserEntity>) {
        dao.insertAll(users)
    }

    suspend fun getUserById(id: String): UserEntity? {
        return dao.getUserById(id)
    }

    suspend fun addUsers(vararg users: UserEntity) {
        users.map {
            dao.addUser(it)
        }
    }

    suspend fun updateUser(user: UserEntity) {
       dao.update(user)
    }

    suspend fun getAllUsers(): List<UserEntity> {
        return dao.getAllUsers()
    }

}