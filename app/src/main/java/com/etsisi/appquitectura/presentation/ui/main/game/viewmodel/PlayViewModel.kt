package com.etsisi.appquitectura.presentation.ui.main.game.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.data.helper.PreferencesHelper
import com.etsisi.appquitectura.data.model.enums.PreferenceKeys
import com.etsisi.appquitectura.domain.enums.GameNavType
import com.etsisi.appquitectura.domain.enums.QuestionLevel
import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.model.AnswerBO
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.domain.model.UserGameScoreBO
import com.etsisi.appquitectura.domain.usecase.GetGameQuestionsUseCase
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent
import com.etsisi.appquitectura.presentation.ui.main.game.model.ClassicGameMode
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemGameMode
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemGameModeAction
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemLabel
import java.util.Calendar
import kotlin.random.Random

class PlayViewModel(
    private val getGameQuestionsUseCase: GetGameQuestionsUseCase,
) : ViewModel(), LifecycleObserver {

    private val _questions by lazy { MutableLiveData<List<QuestionBO>>() }
    val questions: LiveData<List<QuestionBO>>
        get() = _questions

    private val _gameModes by lazy { MutableLiveData<List<ItemGameMode>>() }
    val gameModes: LiveData<List<ItemGameMode>>
        get() = _gameModes

    private val _onShowTopicPicker by lazy { MutableLiveEvent<List<ItemLabel>>() }
    val onShowTopicPicker: LiveEvent<List<ItemLabel>>
        get() = _onShowTopicPicker

    private val _startGame by lazy { MutableLiveEvent<Int>() }
    val startGame: LiveEvent<Int>
        get() = _startGame

    private val _repeatIncorrectAnswers by lazy { MutableLiveEvent<UserGameScoreBO>() }
    val repeatIncorrectAnswers: LiveEvent<UserGameScoreBO>
        get() = _repeatIncorrectAnswers

    private val _showResults by lazy { MutableLiveEvent<Boolean>() }
    val showResults: LiveEvent<Boolean>
        get() = _showResults

    private val _navType = MutableLiveData<GameNavType>()
    val navType: LiveData<GameNavType>
        get() = _navType

    private val _currentTabIndex = MutableLiveData(1)
    val currentTabIndex: LiveData<Int>
        get() = _currentTabIndex

    val _userGameResult by lazy { UserGameScoreBO() }
    var _labelsSelectedIndex: IntArray? = null

    val labelsList = QuestionTopic.values()
            .filter { it != QuestionTopic.UNKNOWN }


    private val mGameModes = listOf(
        ItemGameMode(ItemGameModeAction.ClassicGame(ClassicGameMode.TWENTY_QUESTIONS)),
        ItemGameMode(ItemGameModeAction.ClassicGame(ClassicGameMode.FORTY_QUESTIONS)),
        ItemGameMode(ItemGameModeAction.WeeklyGame),
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

    fun handleGameModeSelected(gameModeIndex: Int, topicsIdSelected: IntArray?) {
        when(getGameModes()[gameModeIndex].action) {
            is ItemGameModeAction.WeeklyGame -> {
                _startGame.value = Event(gameModeIndex)
            }
            is ItemGameModeAction.TestGame -> {
                if (topicsIdSelected != null) {
                    _labelsSelectedIndex = topicsIdSelected
                    _startGame.value = Event(gameModeIndex)
                } else {
                    _onShowTopicPicker.value = Event(
                        labelsList.map { ItemLabel(it) }
                    )
                }
            }
            is ItemGameModeAction.ClassicGame -> {
                _startGame.value = Event(gameModeIndex)
            }
        }
    }

    fun fetchInitialQuestions(gameMode: ItemGameMode, topicsSelected: List<QuestionTopic>?) {
        var topicList: List<QuestionTopic>? = null
        var totalQuestions = 20
        var level = QuestionLevel.EASY
        when (val mode = gameMode.action) {
             is ItemGameModeAction.WeeklyGame -> {
                 topicList = weeklyQuestionsGenerator()
            }
            is ItemGameModeAction.TestGame -> {
                totalQuestions = mode.numberOfQuestions
                topicList = topicsSelected
            }
            is ItemGameModeAction.ClassicGame -> {
                totalQuestions = mode.classicType.numberOfQuestions
            }
        }
        getGameQuestionsUseCase.invoke(
            scope = viewModelScope,
            params = GetGameQuestionsUseCase.Params(
                topics = topicList,
                totalCount = totalQuestions,
                level = level
            )
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

    fun weeklyQuestionsGenerator(): List<QuestionTopic> {
        val topicIndex = Random(seed = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)).nextInt(labelsList.size)
        return listOf (labelsList[topicIndex])
    }

    fun onGameFinished(currentNavType: GameNavType) {
        with(_userGameResult.getAllIncorrectQuestions()) {
            when (currentNavType) {
                GameNavType.REPEAT_INCORRECT_ANSWERS -> {
                    if (isEmpty()) {
                        _showResults.value = Event(true)
                    } else {
                        _repeatIncorrectAnswers.value = Event(_userGameResult)
                    }
                }
                else -> {
                    PreferencesHelper.writeObject(PreferenceKeys.USER_SCORE, _userGameResult)
                    if (isEmpty()) {
                        _showResults.value = Event(true)
                    } else {
                        _repeatIncorrectAnswers.value = Event(_userGameResult)
                    }
                }
            }
        }
    }
}