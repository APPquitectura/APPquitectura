package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.RankingRepository
import com.etsisi.appquitectura.domain.model.RankingBO

class FetchRankingUseCase(private val repository: RankingRepository): UseCase<Unit, List<RankingBO>>() {

    override suspend fun run(params: Unit): List<RankingBO> {
        return repository.fetchRanking()
    }

}