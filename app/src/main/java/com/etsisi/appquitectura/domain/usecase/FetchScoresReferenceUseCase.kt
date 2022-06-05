package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.RankingRepository
import com.etsisi.appquitectura.domain.model.ScoreBO

class FetchScoresReferenceUseCase(
    private val respository: RankingRepository
): UseCase<Unit, List<ScoreBO>>() {
    override suspend fun run(params: Unit): List<ScoreBO> {
       return respository.fetchScoresReference()
    }

}