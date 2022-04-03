package com.etsisi.appquitectura.data.datasource.local

import com.etsisi.appquitectura.data.datasource.local.dao.QuestionsDAO
import com.etsisi.appquitectura.domain.model.QuestionBO

class QuestionsLocalDataSource(
    private val dao: QuestionsDAO
) {
    suspend fun fetchQuestions(): List<QuestionBO>? = dao.getQuestions()?.map { it.toDomain() }

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun addAll(questions: List<QuestionBO>) {
        val questionsList = questions.mapNotNull { it.toEntity() }
        dao.insertAll(questionsList)
    }
}