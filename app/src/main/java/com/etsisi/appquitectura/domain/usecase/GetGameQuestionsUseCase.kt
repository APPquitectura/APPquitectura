package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.domain.model.QuestionLevel

class GetGameQuestionsUseCase(
    private val questionsRepository: QuestionsRepository
): UseCase<GetGameQuestionsUseCase.Params, List<QuestionBO>>() {
    data class Params(
        val level: QuestionLevel,
        val totalCount: Int
    )

    override suspend fun run(params: Params): List<QuestionBO> {
        return questionsRepository.getGameQuestions(params.level, params.totalCount) ?: emptyList()
    }
}