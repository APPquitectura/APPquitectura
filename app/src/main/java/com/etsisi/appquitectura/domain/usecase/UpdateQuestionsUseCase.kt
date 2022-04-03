package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.model.QuestionSubject
import com.etsisi.appquitectura.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async

class UpdateQuestionsUseCase(
        private val usersRepository: UsersRepository,
        private val questionsRepository: QuestionsRepository
): UseCase<Unit, Unit>() {

    override suspend fun run(params: Unit) {
        val currentUserSubject = runCatching {
                usersRepository.getUserById(CurrentUser.email.orEmpty())?.subject
            }.getOrDefault(QuestionSubject.COMPOSICION)

        with(MainScope()) {
            val deleteJob = async(Dispatchers.IO) {
                questionsRepository.deleteAllLocalQuestions()
            }
            val downloadJob = async(Dispatchers.IO) {
                val collection = when(currentUserSubject) {
                    QuestionSubject.COMPOSICION -> {
                        Constants.questions_composicion_collection
                    }
                    else -> {
                        Constants.questions_introduccion_collection
                    }
                }
                questionsRepository.fetchQuestions(collection, currentUserSubject)
            }
            deleteJob.await()
            downloadJob.await()
        }
    }

}