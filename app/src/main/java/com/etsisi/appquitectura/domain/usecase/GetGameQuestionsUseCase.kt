package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.domain.enums.QuestionLevel
import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.model.QuestionBO

class GetGameQuestionsUseCase(
    private val questionsRepository: QuestionsRepository
): UseCase<GetGameQuestionsUseCase.Params, List<QuestionBO>>() {
    data class Params(
        val level: QuestionLevel,
        val totalCount: Int,
        val topics: List<QuestionTopic>? = null
    )

    override suspend fun run(params: Params): List<QuestionBO> {
        return questionsRepository.getGameQuestions(params.level, params.totalCount, params.topics) ?: emptyList()
    }
}