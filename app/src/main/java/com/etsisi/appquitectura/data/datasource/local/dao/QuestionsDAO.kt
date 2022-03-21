package com.etsisi.appquitectura.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.etsisi.appquitectura.data.model.entities.QuestionEntity

@Dao
interface QuestionsDAO: BaseDAO<QuestionEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<QuestionEntity>)

    @Query("SELECT * FROM questions")
    suspend fun getQuestions(): List<QuestionEntity>

}