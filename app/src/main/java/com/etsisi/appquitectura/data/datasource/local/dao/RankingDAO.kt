package com.etsisi.appquitectura.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.etsisi.appquitectura.data.model.entities.ScoreEntity

@Dao
interface RankingDAO: BaseDAO<ScoreEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(scores: List<ScoreEntity>)

    @Query("SELECT * FROM scores")
    suspend fun fetchScoresReference(): List<ScoreEntity>
}