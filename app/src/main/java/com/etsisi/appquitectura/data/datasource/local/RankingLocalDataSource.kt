package com.etsisi.appquitectura.data.datasource.local

import com.etsisi.appquitectura.data.datasource.local.dao.RankingDAO
import com.etsisi.appquitectura.data.model.entities.ScoreEntity

class RankingLocalDataSource(
    private val dao: RankingDAO
) {
    suspend fun fetchScoresReference(): List<ScoreEntity> {
        return dao.fetchScoresReference()
    }

    suspend fun addScoresReference(scores: List<ScoreEntity>) {
        dao.insertAll(scores)
    }
}