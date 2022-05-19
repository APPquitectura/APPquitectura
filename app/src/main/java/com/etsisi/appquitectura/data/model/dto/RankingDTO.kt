package com.etsisi.appquitectura.data.model.dto

import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.enums.RankingType
import com.etsisi.appquitectura.domain.model.RankingBO
import com.etsisi.appquitectura.domain.model.UserBO
import com.etsisi.appquitectura.presentation.utils.EMPTY

data class RankingDTO(
    val id: String = String.EMPTY,
    val rankingPoints: Map<String, Int> = mapOf(),
    val weeklyTopic: String? = null
) {
    fun toDomain(userBO: UserBO): RankingBO {
        val points = mutableMapOf<RankingType, Int>()
        rankingPoints.mapKeys {
            points.put(RankingType.parse(it.key), it.value)
        }
        return RankingBO(
            id = id,
            user = userBO,
            rankingPoints = points,
            weeklyTopic = weeklyTopic?.let { QuestionTopic.parseTopic(it) } ?: QuestionTopic.UNKNOWN
        )
    }
}


