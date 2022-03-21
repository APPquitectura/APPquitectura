package com.etsisi.appquitectura.data.workers

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.utils.Constants.questions_collection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class QuestionsWorker (appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params), KoinComponent {

    private val questionsRepository: QuestionsRepository by inject()

    companion object {

        private const val WORKER_QUESTIONS_ID = "questionsId"

        fun fetchAllQuestions(context: Context) {
            val constraints: Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<QuestionsWorker>()
                .setConstraints(constraints)
                .build()


            WorkManager
                .getInstance(context)
                .enqueueUniqueWork(WORKER_QUESTIONS_ID, ExistingWorkPolicy.KEEP, workRequest)
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            questionsRepository.fetchQuestions(questions_collection)?.let { list ->
                Result.success()
            } ?: Result.failure()
        } catch (e: Exception) {
            Log.e(TAG, e.message.orEmpty())
            Result.failure()
        }
    }
}