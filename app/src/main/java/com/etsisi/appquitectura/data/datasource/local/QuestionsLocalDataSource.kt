package com.etsisi.appquitectura.data.datasource.local

import android.util.Log
import com.etsisi.appquitectura.data.datasource.local.dao.QuestionsDAO
import com.etsisi.appquitectura.data.model.entities.QuestionEntity
import com.etsisi.appquitectura.domain.enums.QuestionLevel
import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.utils.TAG
import java.lang.Exception

class QuestionsLocalDataSource(
    private val dao: QuestionsDAO
) {
    suspend fun fetchQuestions(): List<QuestionBO>? = dao.getQuestions()?.map { it.toDomain() }

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun addAll(questions: List<QuestionEntity>) {
        dao.insertAll(questions)
    }

    suspend fun getQuestionsByTopics(level: QuestionLevel, topics: List<QuestionTopic>?): List<QuestionBO> {
        return dao.getQuestionsByLevel(level.value).map { entity ->
            entity.toDomain().takeIf { questionBO ->
                topics?.let { questionBO.labels.intersect(topics).isNotEmpty() } ?: true
            }
        }.filterNotNull()
    }
}