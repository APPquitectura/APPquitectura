package com.etsisi.appquitectura.data.datasource.remote

import android.util.Log
import com.etsisi.appquitectura.data.helper.FirestoreHelper
import com.etsisi.appquitectura.data.model.dto.ScoreDTO
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.utils.Constants
import kotlinx.coroutines.suspendCancellableCoroutine

class ScoresRemoteDataSource {
    suspend fun fetchScoresReference(): ScoreDTO? = suspendCancellableCoroutine { cont ->
        FirestoreHelper
            .readDocument<ScoreDTO>(
                collection = Constants.score_collection,
                document = "reference",
                onSuccess = {
                    cont.resume(it, null)
                },
                onError = {
                    Log.e(TAG, it.message.orEmpty())
                    cont.resume(null, null)
                }
            )
    }
}