package com.etsisi.appquitectura.domain.usecase

import android.util.Log
import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.utils.TAG

class FetchAllQuestionsUseCase(
    private val questionsRepository: QuestionsRepository
) : UseCase<Unit, List<QuestionBO>>() {

    override suspend fun run(params: Unit): List<QuestionBO> {
        return CurrentUser.email?.let {
            runCatching {
                questionsRepository.fetchQuestions()
            }.getOrElse {
                Log.e(TAG, it.toString())
                emptyList()
            }
        } ?: emptyList()
    }

}