package com.etsisi.appquitectura.data.datasource.local

import com.etsisi.appquitectura.data.datasource.local.dao.QuestionsDAO
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.domain.model.QuestionLevel

class QuestionsLocalDataSource(
    private val dao: QuestionsDAO
) {
    suspend fun fetchQuestions(): List<QuestionBO>? = dao.getQuestions()?.map { it.toDomain() }

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun addAll(questions: List<QuestionBO>) {
        val questionsList = questions.mapNotNull { it.toEntity() }
        dao.insertAll(questionsList)
    }

    suspend fun getCustomQuestions(level: QuestionLevel, totalCount: Int): List<QuestionBO> {
        return dao.getCustomQuestions(level.value, totalCount).map { it.toDomain() }
    }
}