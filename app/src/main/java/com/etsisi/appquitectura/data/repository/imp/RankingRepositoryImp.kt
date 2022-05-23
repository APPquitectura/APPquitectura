package com.etsisi.appquitectura.data.repository.imp

import com.etsisi.appquitectura.data.datasource.local.RankingLocalDataSource
import com.etsisi.appquitectura.data.datasource.remote.RankingRemoteDataSource
import com.etsisi.appquitectura.data.repository.RankingRepository
import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.enums.RankingType
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

    override suspend fun updateRanking(
        id: String,
        points: Int,
        rankingType: RankingType,
        weeklyTopic: QuestionTopic
    ) {
        if (rankingType != RankingType.UNKOWN) {
            getRankingInfoById(id)?.let { rankingInfo ->
                when (rankingType) {
                    RankingType.GENERAL -> {
                        val weeklyPointsUntilNow = rankingInfo.rankingPoints[RankingType.WEEKLY] ?: 0
                        rankingInfo.copy(
                            rankingPoints = mapOf(
                                RankingType.GENERAL to points,
                                RankingType.WEEKLY to weeklyPointsUntilNow
                            )
                        )
                            .toDTO()
                            .also {
                                remote.updateRanking(it)
                            }
                    }
                    RankingType.WEEKLY -> {
                        val generalPointsUntilNow = rankingInfo.rankingPoints[RankingType.GENERAL] ?: 0
                        rankingInfo.copy(
                            rankingPoints = mapOf(
                                RankingType.GENERAL to generalPointsUntilNow,
                                RankingType.WEEKLY to points
                            ),
                            weeklyTopic = weeklyTopic
                        )
                            .toDTO()
                            .also {
                                remote.updateRanking(it)
                            }
                    }
                    else -> {}
                }
            } ?: run {
                //Create document
                when (rankingType) {
                    RankingType.GENERAL -> {
                       RankingBO (
                           id = id,
                           user = null,
                           rankingPoints = mapOf(
                               RankingType.GENERAL to points,
                               RankingType.WEEKLY to 0
                           ),
                           weeklyTopic = weeklyTopic
                       )
                           .toDTO()
                           .also {
                               remote.updateRanking(it)
                           }
                    }
                    RankingType.WEEKLY -> {
                        RankingBO (
                            id = id,
                            user = null,
                            rankingPoints = mapOf(
                                RankingType.GENERAL to 0,
                                RankingType.WEEKLY to points
                            ),
                            weeklyTopic = weeklyTopic
                        )
                            .toDTO()
                            .also {
                                remote.updateRanking(it)
                            }
                    }
                    else -> {}
                }
            }
        }
    }

    private suspend fun getRankingInfoById(id: String): RankingBO? {
        return remote.getRankingInfoById(id)?.toDomain()
    }

    private suspend fun getUserById(id: String): UserBO? {
        return local.getUserById(id)?.toDomain()
    }
}