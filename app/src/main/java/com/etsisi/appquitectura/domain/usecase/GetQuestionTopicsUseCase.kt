package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.domain.enums.QuestionTopic

class GetQuestionTopicsUseCase {
    fun invoke(): List<QuestionTopic> {
         return QuestionTopic.values().toList()
    }
}