package com.etsisi.appquitectura.presentation.ui.main.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.domain.enums.GameNavType
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.domain.model.QuestionLevel
import com.etsisi.appquitectura.domain.model.QuestionSubject
import com.etsisi.appquitectura.domain.usecase.GetGameQuestionsUseCase
import com.etsisi.appquitectura.presentation.ui.main.model.ItemGameMode
import com.etsisi.appquitectura.presentation.ui.main.model.ItemGameModeAction
import com.etsisi.appquitectura.utils.Constants
import kotlinx.coroutines.launch

class PlayViewModel(
    private val getGameQuestionsUseCase: GetGameQuestionsUseCase
) : ViewModel(), LifecycleObserver {

    private val _questions by lazy { MutableLiveData<List<QuestionBO>>() }
    val questions: LiveData<List<QuestionBO>>
        get() = _questions

    private val _navType = MutableLiveData<GameNavType>()
    val navType: LiveData<GameNavType>
        get() = _navType

    val gameModes = listOf(
        ItemGameMode(R.string.game_mode_thirty, ItemGameModeAction.THIRTY_QUESTIONS),
        ItemGameMode(R.string.game_mode_sixty, ItemGameModeAction.SIXTY_QUESTIONS)
    )

    fun setNavType(navType: GameNavType) {
        _navType.value = navType
    }

    fun fetchInitialQuestions(navType: ItemGameModeAction) {
        when (navType) {
            ItemGameModeAction.THIRTY_QUESTIONS -> {
                getGameQuestions(QuestionLevel.EASY, 30)
            }
            ItemGameModeAction.SIXTY_QUESTIONS -> {
                getGameQuestions(QuestionLevel.EASY, 30)
            }
        }
    }

    fun getGameQuestions(level: QuestionLevel, totalQuestions: Int) {
        getGameQuestionsUseCase.invoke(
            scope = viewModelScope,
            params = GetGameQuestionsUseCase.Params(level, totalQuestions)
        ) {
            _questions.value = it
        }
    }
}