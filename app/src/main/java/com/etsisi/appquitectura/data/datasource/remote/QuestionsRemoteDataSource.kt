package com.etsisi.appquitectura.data.datasource.remote

import android.util.Log
import com.etsisi.appquitectura.data.helper.FirestoreHelper
import com.etsisi.appquitectura.data.model.dto.QuestionDTO
import com.etsisi.appquitectura.presentation.utils.TAG
import kotlinx.coroutines.suspendCancellableCoroutine

class QuestionsRemoteDataSource {

    suspend fun fetchQuestions(collection: String): List<QuestionDTO>? = suspendCancellableCoroutine { cont ->
        FirestoreHelper
            .readDocumentsOfACollection<QuestionDTO>(collection,
                onSuccess = {
                    cont.resume(it, null)
                },
                onError = {
                    Log.e(TAG, it.message.orEmpty())
                    cont.resume(null, null)
                })
    }
}