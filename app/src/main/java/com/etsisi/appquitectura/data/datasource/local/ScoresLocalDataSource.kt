package com.etsisi.appquitectura.data.datasource.local

import com.etsisi.appquitectura.data.datasource.local.dao.ScoreDAO
import com.etsisi.appquitectura.data.model.entities.ScoreEntity

class ScoresLocalDataSource(
    private val dao: ScoreDAO
) {
    suspend fun fetchScoresReference(): List<ScoreEntity> {
        return dao.fetchScoresReference()
    }

    suspend fun addScoresReference(scores: List<ScoreEntity>) {
        dao.insertAll(scores)
    }
}