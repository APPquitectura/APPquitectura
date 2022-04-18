package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.QuestionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async

class UpdateQuestionsUseCase(
    private val questionsRepository: QuestionsRepository
) : UseCase<Unit, Unit>() {

    override suspend fun run(params: Unit) {
        with(MainScope()) {
            val deleteJob = async(Dispatchers.IO) {
                questionsRepository.deleteAllLocalQuestions()
            }
            val downloadJob = async(Dispatchers.IO) {
                questionsRepository.fetchQuestions()
            }
            deleteJob.await()
            downloadJob.await()
        }
    }
}