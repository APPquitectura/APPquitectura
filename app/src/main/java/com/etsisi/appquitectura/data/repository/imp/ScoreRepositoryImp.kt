package com.etsisi.appquitectura.data.repository.imp

import android.util.Log
import com.etsisi.appquitectura.data.datasource.local.ScoresLocalDataSource
import com.etsisi.appquitectura.data.datasource.remote.ScoresRemoteDataSource
import com.etsisi.appquitectura.data.repository.ScoreRepository
import com.etsisi.appquitectura.domain.model.ScoreBO

class ScoreRepositoryImp(
    private val remote: ScoresRemoteDataSource,
    private val local: ScoresLocalDataSource
) : ScoreRepository {
    override suspend fun fetchScoresReference(): List<ScoreBO> {
        return local.fetchScoresReference().map { it.toDomain() }.ifEmpty {
            remote.fetchScoresReference()?.toDomain()?.also { list ->
                local.addScoresReference(list.map { it.toEntity() })
        }
        } ?: emptyList()
    }
}