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

    @Query("DELETE FROM questions")
    suspend fun deleteAll()

    @Query("SELECT * FROM questions")
    suspend fun getQuestions(): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE level = :level")
    suspend fun getQuestionsByLevel(level: String): List<QuestionEntity>

}