package com.etsisi.appquitectura.domain.usecase

import android.util.Log
import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.domain.model.QuestionSubject
import com.etsisi.appquitectura.presentation.utils.EMPTY
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.utils.Constants

class FetchAllQuestionsUseCase(
    private val usersRepository: UsersRepository,
    private val questionsRepository: QuestionsRepository
) : UseCase<Unit, List<QuestionBO>>() {

    override suspend fun run(params: Unit): List<QuestionBO> {
        return CurrentUser.email?.let {
            runCatching {
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
                questionsRepository.fetchQuestions(collection, currentUser.subject)
            }.getOrElse {
                Log.e(TAG, it.toString())
                emptyList()
            }
        } ?: emptyList()
    }

}