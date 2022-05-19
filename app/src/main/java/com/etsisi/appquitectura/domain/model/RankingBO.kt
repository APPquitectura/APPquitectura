package com.etsisi.appquitectura.domain.model

import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.enums.RankingType

data class RankingBO(
    val id: String,
    val user: UserBO,
    val rankingPoints: Map<RankingType, Int>,
    val weeklyTopic: QuestionTopic
)
