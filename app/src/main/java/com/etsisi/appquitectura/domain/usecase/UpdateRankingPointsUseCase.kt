package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.RankingRepository
import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.enums.RankingType

class UpdateRankingPointsUseCase(
    private val rankingRepository: RankingRepository
): UseCase<UpdateRankingPointsUseCase.Params, Unit>() {

    data class Params(
        val id: String,
        val points: Int,
        val rankingType: RankingType,
        val weeklyTopic: QuestionTopic
    )

    override suspend fun run(params: Params) {
        rankingRepository.updateRanking(params.id, params.points, params.rankingType, params.weeklyTopic)
    }
}