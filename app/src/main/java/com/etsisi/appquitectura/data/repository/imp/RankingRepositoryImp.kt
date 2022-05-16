package com.etsisi.appquitectura.data.repository.imp

import com.etsisi.appquitectura.data.datasource.local.RankingLocalDataSource
import com.etsisi.appquitectura.data.datasource.remote.RankingRemoteDataSource
import com.etsisi.appquitectura.data.repository.RankingRepository
import com.etsisi.appquitectura.domain.model.ScoreBO

class RankingRepositoryImp(
    private val remote: RankingRemoteDataSource,
    private val local: RankingLocalDataSource
) : RankingRepository {
    override suspend fun fetchScoresReference(): List<ScoreBO> {
        return local.fetchScoresReference().map { it.toDomain() }.ifEmpty {
            remote.fetchScoresReference()?.toDomain()?.also { list ->
                local.addScoresReference(list.map { it.toEntity() })
        }
        } ?: emptyList()
    }
}