package com.etsisi.appquitectura.data.repository

import com.etsisi.appquitectura.domain.model.QuestionBO

interface QuestionsRepository {
    suspend fun fetchQuestions(collection: String): List<QuestionBO>?

    suspend fun addAllQuestions(list: List<QuestionBO>)
}