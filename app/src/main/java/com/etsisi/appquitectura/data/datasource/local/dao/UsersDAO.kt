package com.etsisi.appquitectura.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.etsisi.appquitectura.data.model.entities.UserEntity

@Dao
interface UsersDAO: BaseDAO<UserEntity> {

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity

}