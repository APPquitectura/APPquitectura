package com.etsisi.appquitectura.data.repository

import com.etsisi.appquitectura.domain.enums.QuestionLevel
import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.model.QuestionBO

interface QuestionsRepository {
    suspend fun fetchQuestions(): List<QuestionBO>?

    suspend fun addAllQuestions(list: List<QuestionBO>)

    suspend fun deleteAllLocalQuestions()

    suspend fun getGameQuestions(
        level: QuestionLevel,
        totalCount: Int,
        topics: List<QuestionTopic>?
    ): List<QuestionBO>?
}