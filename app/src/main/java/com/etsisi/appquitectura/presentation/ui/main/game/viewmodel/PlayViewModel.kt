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
import com.etsisi.appquitectura.domain.enums.ClassicGameType
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemGameMode
import com.etsisi.appquitectura.domain.enums.GameType
import com.etsisi.appquitectura.domain.enums.RankingType
import com.etsisi.appquitectura.domain.usecase.GetQuestionTopicsUseCase
import com.etsisi.appquitectura.domain.usecase.GetWeeklyQuestionTopicUseCase
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemLabel

class PlayViewModel(
    private val getGameQuestionsUseCase: GetGameQuestionsUseCase,
    private val getQuestionTopicsUseCase: GetQuestionTopicsUseCase,
    private val getWeeklyQuestionTopicUseCase: GetWeeklyQuestionTopicUseCase
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

    private val _repeatIncorrectAnswers by lazy { MutableLiveEvent<Array<QuestionBO>>() }
    val repeatIncorrectAnswers: LiveEvent<Array<QuestionBO>>
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

    var levelOfNextQuestions: QuestionLevel? = null
    var gameModeSelected: ItemGameMode? = null
    var topicsSelectedToFilter: List<QuestionTopic>? = null
    var rankingType: RankingType? = null
    var _labelsSelectedIndex: IntArray? = null
    val _userGameResult by lazy { UserGameScoreBO(rankingType = rankingType) }
    val labelsList by lazy { getQuestionTopicsUseCase.invoke().filter { it != QuestionTopic.UNKNOWN } }

    private companion object {
        const val DEFAULT_INITIAL_QUESTIONS_COUNT = 20
    }


    private val mGameModes = listOf(
        ItemGameMode(GameType.ClassicGame(ClassicGameType.TWENTY_QUESTIONS)),
        ItemGameMode(GameType.ClassicGame(ClassicGameType.FORTY_QUESTIONS)),
        ItemGameMode(GameType.WeeklyGame),
        ItemGameMode(GameType.TestGame(20, labelsList))
    )

    fun setTopicsSelected(topicsIndexList: List<Int>?) {
        topicsSelectedToFilter = topicsIndexList?.map { labelsList[it] }
    }

    fun setGameModeSelected(gameModeIndex: Int) {
        if (gameModeIndex > -1) {
            gameModeSelected = getGameModes()[gameModeIndex]
        }
    }

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
            is GameType.WeeklyGame -> {
                _startGame.value = Event(gameModeIndex)
            }
            is GameType.TestGame -> {
                if (topicsIdSelected != null) {
                    _labelsSelectedIndex = topicsIdSelected
                    _startGame.value = Event(gameModeIndex)
                } else {
                    _onShowTopicPicker.value = Event(
                        labelsList.map { ItemLabel(it) }
                    )
                }
            }
            is GameType.ClassicGame -> {
                _startGame.value = Event(gameModeIndex)
            }
        }
    }

    fun fetchInitialQuestions() {
        var topicList: List<QuestionTopic>? = null
        var totalQuestions = DEFAULT_INITIAL_QUESTIONS_COUNT
        val initialLevel = QuestionLevel.EASY
        gameModeSelected?.let { mode ->
            when (mode.action) {
                is GameType.WeeklyGame -> {
                    rankingType = RankingType.WEEKLY
                    topicList = weeklyQuestionsGenerator()
                }
                is GameType.TestGame -> {
                    rankingType = RankingType.UNKOWN
                    totalQuestions = mode.action.numberOfQuestions
                    topicList = topicsSelectedToFilter
                }
                is GameType.ClassicGame -> {
                    rankingType = RankingType.GENERAL
                    totalQuestions = mode.action.classicType.numberOfQuestions
                }
            }
            getGameQuestionsUseCase.invoke(
                scope = viewModelScope,
                params = GetGameQuestionsUseCase.Params(
                    topics = topicList,
                    totalCount = totalQuestions,
                    level = initialLevel
                )
            ) {
                setQuestions(it)
            }
        }
    }

    fun fetchNextQuestion(smoothNextPage: () -> Unit) {
        getGameQuestionsUseCase.invoke(
            scope = viewModelScope,
            params = GetGameQuestionsUseCase.Params(
                topics = topicsSelectedToFilter,
                totalCount = 1,
                level = levelOfNextQuestions ?: _userGameResult.getLevelOfNextQuestion()
            )
        ) {
            _questions.value?.let { questionsUntilNow ->
                val new = mutableListOf<QuestionBO>()
                new.addAll(questionsUntilNow)
                new.set(index = currentTabIndex.value ?: 0, element = it.first { !new.contains(it) })
                _questions.value = new
            }
            smoothNextPage()
        }
    }

    fun setQuestions(questionsList: List<QuestionBO>) {
        _questions.value = questionsList.map { question ->
            question.copy(answers = question.answers.asSequence().shuffled().toList())
        }
    }

    fun setGameResultAccumulated(question: QuestionBO, userAnswer: AnswerBO, points: Long, userMarkInMillis: Long, isGameFinished: Boolean, smoothNextPage: () -> Unit) {
        _navType.value?.let { currentNavType ->
            if (currentNavType == GameNavType.START_GAME) {
                _userGameResult.apply {
                    userQuestions.add(question)
                    this.userAnswer.add(Pair(userAnswer, points))
                    totalTime += userMarkInMillis
                }
            }
            if (isGameFinished) {
                onGameFinished(currentNavType)
            } else {
                fetchNextQuestion(smoothNextPage)
            }
        }
    }

    fun onGameFinished(currentNavType: GameNavType) {
        with(_userGameResult.getAllIncorrectQuestions()) {
            when (currentNavType) {
                GameNavType.REPEAT_INCORRECT_ANSWERS -> {
                    if (isEmpty()) {
                        _showResults.value = Event(true)
                    } else {
                        _repeatIncorrectAnswers.value = Event(_userGameResult.getAllIncorrectQuestions().toTypedArray())
                    }
                }
                else -> {
                    PreferencesHelper.writeObject(PreferenceKeys.USER_SCORE, _userGameResult)
                    if (isEmpty()) {
                        _showResults.value = Event(true)
                    } else {
                        _repeatIncorrectAnswers.value = Event(_userGameResult.getAllIncorrectQuestions().toTypedArray())
                    }
                }
            }
        }
    }

    private fun weeklyQuestionsGenerator(): List<QuestionTopic> {
        return getWeeklyQuestionTopicUseCase
            .invoke(GetWeeklyQuestionTopicUseCase.Params(topics = labelsList))
            .let {
                listOf(it)
            }
    }
}