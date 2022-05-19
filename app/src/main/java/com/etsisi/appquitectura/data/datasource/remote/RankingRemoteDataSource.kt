package com.etsisi.appquitectura.data.datasource.remote

import android.util.Log
import com.etsisi.appquitectura.data.helper.FirestoreHelper
import com.etsisi.appquitectura.data.model.dto.RankingDTO
import com.etsisi.appquitectura.data.model.dto.ScoreDTO
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.utils.Constants
import kotlinx.coroutines.suspendCancellableCoroutine

class RankingRemoteDataSource {
    suspend fun fetchScoresReference(): ScoreDTO? = suspendCancellableCoroutine { cont ->
        FirestoreHelper
            .readDocument<ScoreDTO>(
                collection = Constants.score_collection,
                document = Constants.score_reference_document,
                onSuccess = {
                    cont.resume(it, null)
                },
                onError = {
                    Log.e(TAG, it.message.orEmpty())
                    cont.resume(null, null)
                }
            )
    }

    suspend fun fetchRanking(): List<RankingDTO>? = suspendCancellableCoroutine { cont ->
        FirestoreHelper
            .readDocumentsOfACollection<RankingDTO>(
                collection = Constants.general_ranking_collection,
                onSuccess = {
                    cont.resume(it, null)
                },
                onError = {
                    Log.e(TAG, it.message.orEmpty())
                    cont.resume(null, null)
                }
            )
    }

    suspend fun getRankingInfoById(id: String): RankingDTO? = suspendCancellableCoroutine { cont ->
        FirestoreHelper
            .readDocument<RankingDTO>(
                collection = Constants.general_ranking_collection,
                document = id,
                onSuccess = {
                    cont.resume(it, null)
                },
                onError = {
                    Log.e(TAG, it.message.orEmpty())
                    cont.resume(null, null)
                }
            )
    }

    fun updateRanking(rankingInfoUpdated: RankingDTO) {
        FirestoreHelper
            .writeDocument(
                collection = Constants.general_ranking_collection,
                document = rankingInfoUpdated.id,
                data = rankingInfoUpdated,
                onSuccess = {
                    Log.e(TAG, "SUCCESS")
                },
                onError = {
                    Log.e(TAG, it.message.orEmpty())
                }
            )
    }
}