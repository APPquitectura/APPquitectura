package com.etsisi.appquitectura.data.repository

import com.etsisi.appquitectura.domain.model.ScoreBO

interface ScoreRepository {

    suspend fun fetchScoresReference(): List<ScoreBO>
}