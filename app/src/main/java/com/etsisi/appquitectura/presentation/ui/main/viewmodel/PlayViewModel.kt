package com.etsisi.appquitectura.presentation.ui.main.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.ui.main.model.ItemGameMode
import com.etsisi.appquitectura.presentation.ui.main.model.ItemGameModeAction
import com.etsisi.appquitectura.utils.Constants
import kotlinx.coroutines.launch

class PlayViewModel(
    private val questionsRepository: QuestionsRepository
): ViewModel(), LifecycleObserver {

    private val _questions by lazy { MutableLiveData<List<QuestionBO>>() }
    val questions: LiveData<List<QuestionBO>>
        get() = _questions

    val gameModes = listOf(
        ItemGameMode(R.string.game_mode_thirty, ItemGameModeAction.THIRTY_QUESTIONS),
        ItemGameMode(R.string.game_mode_sixty, ItemGameModeAction.SIXTY_QUESTIONS)
    )

    init {
        viewModelScope.launch {
            _questions.value = questionsRepository.fetchQuestions(Constants.questions_collection)
        }
    }

}