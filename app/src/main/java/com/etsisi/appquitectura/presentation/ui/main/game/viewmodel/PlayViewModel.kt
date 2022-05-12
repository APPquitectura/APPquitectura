package com.etsisi.appquitectura.presentation.ui.main.game.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.domain.enums.GameNavType
import com.etsisi.appquitectura.domain.model.AnswerBO
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.domain.model.QuestionLevel
import com.etsisi.appquitectura.domain.model.QuestionTopic
import com.etsisi.appquitectura.domain.model.UserGameScoreBO
import com.etsisi.appquitectura.domain.usecase.GetGameQuestionsUseCase
import com.etsisi.appquitectura.presentation.ui.main.game.model.ClassicGameMode
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemGameMode
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemGameModeAction
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemLabel
import com.etsisi.appquitectura.presentation.ui.main.game.model.QuestionTopics

class PlayViewModel(
    private val getGameQuestionsUseCase: GetGameQuestionsUseCase,
) : ViewModel(), LifecycleObserver {

    private val _questions by lazy { MutableLiveData<List<QuestionBO>>() }
    val questions: LiveData<List<QuestionBO>>
        get() = _questions

    private val _gameModes by lazy { MutableLiveData<List<ItemGameMode>>() }
    val gameModes: LiveData<List<ItemGameMode>>
    get() = _gameModes

    private val _navType = MutableLiveData<GameNavType>()
    val navType: LiveData<GameNavType>
        get() = _navType

    private val _currentTabIndex = MutableLiveData(1)
    val currentTabIndex: LiveData<Int>
        get() = _currentTabIndex

    val _userGameResult = UserGameScoreBO()

    private val labelsList = QuestionTopics().apply {
        QuestionTopic.values()
            .filter { it != QuestionTopic.UNKNOWN }
            .onEach { add(it) }
    }

    private val mGameModes = listOf(
        ItemGameMode(ItemGameModeAction.WeeklyGame),
        ItemGameMode(ItemGameModeAction.ClassicGame(ClassicGameMode.TWENTY_QUESTIONS, ClassicGameMode.FORTY_QUESTIONS)),
        ItemGameMode(ItemGameModeAction.TestGame(20, labelsList))
    )

    fun setNavType(navType: GameNavType) {
        _navType.value = navType
    }

    fun setCurrentTabIndex(index: Int) {
        _currentTabIndex.value = index
    }

    fun getGameModes(): List<ItemGameMode> {
        return mGameModes.also { _gameModes.value = it }
    }

    fun fetchInitialQuestions(gameMode: ItemGameMode) {
        getGameQuestionsUseCase.invoke(
            scope = viewModelScope,
            params = GetGameQuestionsUseCase.Params(QuestionLevel.EASY, 20)
        ) {
            setQuestions(it)
        }
    }

    fun setQuestions(questionsList: List<QuestionBO>) {
        _questions.value = questionsList.map { question ->
            question.copy(answers = question.answers.asSequence().shuffled().toList())
        }
    }

    fun setGameResultAccumulated(
        question: QuestionBO,
        userAnswer: AnswerBO,
        points: Long,
        userMarkInMillis: Long
    ) {
        _userGameResult.apply {
            userQuestions.add(question)
            this.userAnswer.add(Pair(userAnswer, points))
            totalTime += userMarkInMillis
        }
    }
}