package com.etsisi.appquitectura.data.datasource.remote

import android.util.Log
import com.etsisi.appquitectura.data.helper.FirestoreHelper
import com.etsisi.appquitectura.data.model.dto.QuestionDTO
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.domain.model.QuestionSubject
import com.etsisi.appquitectura.presentation.utils.TAG
import kotlinx.coroutines.suspendCancellableCoroutine

class QuestionsRemoteDataSource {

    suspend fun fetchQuestions(collection: String, questionsSubject: QuestionSubject): List<QuestionBO>? = suspendCancellableCoroutine { cont ->
        FirestoreHelper
            .readDocumentsOfACollection<QuestionDTO>(collection,
                onSuccess = { list ->
                    val result = list.map { it.toDomain().copy(subject = questionsSubject) }
                    cont.resume(result, null)
                },
                onError = {
                    Log.e(TAG, it.message.orEmpty())
                    cont.resume(null, null)
                })
    }
}