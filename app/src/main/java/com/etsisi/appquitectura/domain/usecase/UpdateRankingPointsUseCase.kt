package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.domain.enums.RankingType

class UpdateRankingPointsUseCase {

    data class Params(
        val id: String,
        val points: Int,
        val rankingType: RankingType
    )
}