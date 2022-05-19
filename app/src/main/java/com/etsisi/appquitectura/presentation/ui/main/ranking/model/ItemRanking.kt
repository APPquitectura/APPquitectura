package com.etsisi.appquitectura.presentation.ui.main.ranking.model

import com.etsisi.appquitectura.domain.enums.RankingType

data class ItemRanking(
    val name: String,
    val rankingPoints: Map<RankingType, Int>,
    val position: Int
)
