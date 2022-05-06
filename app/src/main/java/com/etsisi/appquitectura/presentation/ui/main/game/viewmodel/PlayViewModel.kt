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

    private val _navType = MutableLiveData<GameNavType>()
    val navType: LiveData<GameNavType>
        get() = _navType

    private val _currentTabIndex = MutableLiveData(1)
    val currentTabIndex: LiveData<Int>
        get() = _currentTabIndex

    val _userGameResult = UserGameScoreBO()

    val labelsList = QuestionTopic.values()
        .filter { it != QuestionTopic.UNKNOWN }
        .map {
            ItemLabel(it)
        }
    val gameModes = listOf(
        ItemGameMode(ItemGameModeAction.TWENTY_QUESTIONS),
        ItemGameMode(ItemGameModeAction.FORTY_QUESTIONS)
    )

    fun setNavType(navType: GameNavType) {
        _navType.value = navType
    }

    fun setCurrentTabIndex(index: Int) {
        _currentTabIndex.value = index
    }

    fun getLabelsToFilter(indexOfLabels: List<Int>): QuestionTopics {
        val array = QuestionTopics()
        indexOfLabels.forEach {
            array.add(labelsList[it].topic)
        }
        return array
    }

    fun fetchInitialQuestions(gameMode: ItemGameModeAction, quiestionsTopics: QuestionTopics?) {
        getGameQuestionsUseCase.invoke(
            scope = viewModelScope,
            params = GetGameQuestionsUseCase.Params(QuestionLevel.EASY, gameMode.totalQuestions)
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