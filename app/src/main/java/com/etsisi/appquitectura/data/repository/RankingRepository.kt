package com.etsisi.appquitectura.data.repository

import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.enums.RankingType
import com.etsisi.appquitectura.domain.model.RankingBO
import com.etsisi.appquitectura.domain.model.ScoreBO
import com.etsisi.appquitectura.domain.model.UserBO

interface RankingRepository {
    suspend fun fetchScoresReference(): List<ScoreBO>
    suspend fun fetchRanking(): List<RankingBO>
    suspend fun updateRanking(id: String, points: Int, rankingType: RankingType, weeklyTopic: QuestionTopic)
}