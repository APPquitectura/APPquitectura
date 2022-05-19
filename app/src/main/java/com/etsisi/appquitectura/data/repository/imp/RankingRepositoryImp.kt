package com.etsisi.appquitectura.data.repository.imp

import com.etsisi.appquitectura.data.datasource.local.RankingLocalDataSource
import com.etsisi.appquitectura.data.datasource.remote.RankingRemoteDataSource
import com.etsisi.appquitectura.data.repository.RankingRepository
import com.etsisi.appquitectura.domain.model.RankingBO
import com.etsisi.appquitectura.domain.model.ScoreBO
import com.etsisi.appquitectura.domain.model.UserBO

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

    override suspend fun fetchRanking(): List<RankingBO> {
        val result = mutableListOf<RankingBO>()
        remote
            .fetchRanking()
            ?.map { itemRanking ->
                getUserById(itemRanking.id)?.let { userBO ->
                    result.add(itemRanking.toDomain(userBO))
                }
            }
        return result
    }

    private suspend fun getUserById(id: String): UserBO? {
        return local.getRankingUserById(id)?.toDomain()
    }
}