package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.domain.model.QuestionSubject

class FetchAllQuestionsUseCase(
    private val questionsRepository: QuestionsRepository
): UseCase<FetchAllQuestionsUseCase.Params, List<QuestionBO>>() {

    data class Params(
        val collection: String,
        val questionsSubject: QuestionSubject
    )

    override suspend fun run(params: Params): List<QuestionBO> {
        return questionsRepository.fetchQuestions(params.collection, params.questionsSubject) ?: emptyList()
    }

}