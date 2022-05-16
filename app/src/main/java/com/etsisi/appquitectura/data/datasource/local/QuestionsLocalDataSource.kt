package com.etsisi.appquitectura.data.datasource.local

import com.etsisi.appquitectura.data.datasource.local.dao.QuestionsDAO
import com.etsisi.appquitectura.data.model.entities.QuestionEntity
import com.etsisi.appquitectura.domain.enums.QuestionLevel
import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.model.QuestionBO

class QuestionsLocalDataSource(
    private val dao: QuestionsDAO
) {
    suspend fun fetchQuestions(): List<QuestionBO>? = dao.getQuestions()?.map { it.toDomain() }

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun addAll(questions: List<QuestionEntity>) {
        dao.insertAll(questions)
    }

    suspend fun getCustomQuestions(
        level: QuestionLevel,
        totalCount: Int,
        topics: List<QuestionTopic>?
    ): List<QuestionBO> {
        return dao.getCustomQuestions(level.value, totalCount).map { entity ->
            entity.toDomain().takeIf { questionBO ->
                topics?.let { questionBO.labels.intersect(topics).isNotEmpty() } ?: true
            }
        }.filterNotNull()
    }
}