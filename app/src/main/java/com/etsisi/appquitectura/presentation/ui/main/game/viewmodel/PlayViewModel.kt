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

    private val labelsList = QuestionTopic.values()
            .filter { it != QuestionTopic.UNKNOWN }


    private val mGameModes = listOf(
        ItemGameMode(ItemGameModeAction.WeeklyGame),
        ItemGameMode(ItemGameModeAction.ClassicGame(ClassicGameMode.TWENTY_QUESTIONS)),
        ItemGameMode(ItemGameModeAction.ClassicGame(ClassicGameMode.FORTY_QUESTIONS)),
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
        val params = when (val mode = gameMode.action) {
             is ItemGameModeAction.WeeklyGame -> {
                GetGameQuestionsUseCase.Params(QuestionLevel.EASY, 20)
            }
            is ItemGameModeAction.TestGame -> {
                val total = mode.numberOfQuestions
                GetGameQuestionsUseCase.Params(QuestionLevel.EASY, total, mode.questionTopics)
            }
            is ItemGameModeAction.ClassicGame -> {
                val total = mode.classicType.numberOfQuestions
                GetGameQuestionsUseCase.Params(QuestionLevel.EASY, total)
            }
        }
        getGameQuestionsUseCase.invoke(
            scope = viewModelScope,
            params = params
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