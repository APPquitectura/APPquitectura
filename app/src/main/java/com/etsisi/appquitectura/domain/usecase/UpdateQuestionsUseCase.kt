package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.model.QuestionSubject
import com.etsisi.appquitectura.presentation.utils.EMPTY
import com.etsisi.appquitectura.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async

class UpdateQuestionsUseCase(
        private val usersRepository: UsersRepository,
        private val questionsRepository: QuestionsRepository
): UseCase<Unit, Unit>() {

    override suspend fun run(params: Unit) {

        CurrentUser.email?.let {
            val currentUser = usersRepository.getUserById(it)
            val collection = when (currentUser.subject) {
                QuestionSubject.COMPOSICION -> {
                    Constants.questions_composicion_collection
                }
                QuestionSubject.INTRODUCCION -> {
                    Constants.questions_introduccion_collection
                }
                else -> {
                    String.EMPTY
                }
            }

            with(MainScope()) {
                val deleteJob = async(Dispatchers.IO) {
                    questionsRepository.deleteAllLocalQuestions()
                }
                val downloadJob = async(Dispatchers.IO) {
                    questionsRepository.fetchQuestions(collection, currentUser.subject)
                }
                deleteJob.await()
                downloadJob.await()
            }
        }
    }

}