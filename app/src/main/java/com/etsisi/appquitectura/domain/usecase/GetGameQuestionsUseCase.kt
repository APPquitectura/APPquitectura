package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.domain.enums.QuestionLevel
import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.model.QuestionBO

class GetGameQuestionsUseCase(
    private val questionsRepository: QuestionsRepository
): UseCase<GetGameQuestionsUseCase.Params, List<QuestionBO>>() {
    data class Params(
        val questionLevel: QuestionLevel,
        val totalCount: Int,
        val topics: List<QuestionTopic>? = null
    )

    override suspend fun run(params: Params): List<QuestionBO> {
        return questionsRepository.getGameQuestions(params.questionLevel, params.totalCount, params.topics).orEmpty()
    }
}