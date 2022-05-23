package com.etsisi.appquitectura.domain.model

import com.etsisi.appquitectura.data.model.dto.RankingDTO
import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.enums.RankingType
import kotlin.math.max

data class RankingBO(
    val id: String,
    val user: UserBO?,
    val rankingPoints: Map<RankingType, Int>,
    val weeklyTopic: QuestionTopic
) {

    fun toDTO(): RankingDTO {
        val points = mutableMapOf<String, Int>()
        rankingPoints.mapKeys {
            points.put(it.key.field, it.value)
        }
        return RankingDTO(
            id = id,
            rankingPoints = points,
            weeklyTopic = weeklyTopic.value
        )
    }

    fun getWeeklyRankingPoints(): Int {
        return rankingPoints[RankingType.WEEKLY] ?: 0
    }
    fun getWeeklyRankingPoints(weeklyTopic: QuestionTopic): Int? {
        return max(0, rankingPoints.getOrElse(RankingType.WEEKLY){0}).takeIf { weeklyTopic == this.weeklyTopic }
    }

    fun getGeneralRankingPoints(): Int? {
        return max(0, rankingPoints.getOrElse(RankingType.GENERAL){0})
    }
}
