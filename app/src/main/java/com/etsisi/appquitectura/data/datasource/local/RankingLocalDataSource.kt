package com.etsisi.appquitectura.data.datasource.local

import com.etsisi.appquitectura.data.datasource.local.dao.ScoreDAO
import com.etsisi.appquitectura.data.datasource.local.dao.UsersDAO
import com.etsisi.appquitectura.data.model.entities.ScoreEntity
import com.etsisi.appquitectura.data.model.entities.UserEntity

class RankingLocalDataSource(
    private val scoreDao: ScoreDAO,
    private val usersDAO: UsersDAO
    ) {
    suspend fun fetchScoresReference(): List<ScoreEntity> {
        return scoreDao.fetchScoresReference()
    }

    suspend fun addScoresReference(scores: List<ScoreEntity>) {
        scoreDao.insertAll(scores)
    }

    suspend fun getUserById(id: String): UserEntity? {
        return usersDAO.getUserById(id)
    }
}