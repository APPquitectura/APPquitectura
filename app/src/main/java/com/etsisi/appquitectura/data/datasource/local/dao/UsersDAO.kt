package com.etsisi.appquitectura.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.etsisi.appquitectura.data.model.entities.UserEntity

@Dao
interface UsersDAO: BaseDAO<UserEntity> {

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(vararg users: UserEntity)

}