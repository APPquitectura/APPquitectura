package com.etsisi.appquitectura.data.repository

import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.domain.model.QuestionLevel
import com.etsisi.appquitectura.domain.model.QuestionSubject

interface QuestionsRepository {
    suspend fun fetchQuestions(collection: String, questionSubject: QuestionSubject): List<QuestionBO>?

    suspend fun addAllQuestions(list: List<QuestionBO>)

    suspend fun deleteAllLocalQuestions()

    suspend fun getGameQuestions(level: QuestionLevel, totalCount: Int): List<QuestionBO>?
}