package com.etsisi.appquitectura.data.repository

import com.etsisi.appquitectura.domain.model.RankingBO
import com.etsisi.appquitectura.domain.model.ScoreBO
import com.etsisi.appquitectura.domain.model.UserBO

interface RankingRepository {
    suspend fun fetchScoresReference(): List<ScoreBO>
    suspend fun fetchRanking(): List<RankingBO>
}