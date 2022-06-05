package com.etsisi.appquitectura.presentation.ui.main.ranking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.enums.RankingType
import com.etsisi.appquitectura.domain.usecase.FetchRankingUseCase
import com.etsisi.appquitectura.domain.usecase.GetQuestionTopicsUseCase
import com.etsisi.appquitectura.domain.usecase.GetWeeklyQuestionTopicUseCase
import com.etsisi.appquitectura.presentation.ui.main.ranking.adapter.RankingViewType
import com.etsisi.appquitectura.presentation.ui.main.ranking.model.ItemRanking

class RankingViewModel(
    private val getQuestionTopicsUseCase: GetQuestionTopicsUseCase,
    private val getWeeklyQuestionTopicUseCase: GetWeeklyQuestionTopicUseCase,
    private val fetchRankingUseCase: FetchRankingUseCase
) : ViewModel() {

    private val _ranking = MutableLiveData<List<ItemRanking>>()
    val ranking: LiveData<List<ItemRanking>>
        get() = _ranking

    private var weeklyTopic: QuestionTopic

    init {
        weeklyTopic = getWeeklyQuestionTopic()
    }

    fun getRanking(rankingType: RankingType) {
        fetchRankingUseCase.invoke(
            scope = viewModelScope,
            params = Unit,
            onResult = { rankingList ->
                if (rankingList.isNotEmpty()) {
                    _ranking.value = when(rankingType) {
                        RankingType.GENERAL -> {
                            rankingList
                                .filter { it.getGeneralRankingPoints() != null }
                                .takeIf { it.isNotEmpty() }
                                ?.mapIndexed { index, rankingBO ->
                                    ItemRanking(
                                        name = rankingBO.user?.name.orEmpty(),
                                        rankingPoints = rankingBO.getGeneralRankingPoints() ?: 0,
                                        position = index + 1,
                                        viewType = RankingViewType.ITEM
                                    )
                                }
                        }
                        else -> {
                            rankingList
                                .filter { it.getWeeklyRankingPoints(weeklyTopic) != null }
                                .takeIf { it.isNotEmpty() }
                                ?.mapIndexed { index, rankingBO ->
                                    ItemRanking(
                                        name = rankingBO.user?.name.orEmpty(),
                                        rankingPoints = rankingBO.getWeeklyRankingPoints(),
                                        position = index + 1,
                                        viewType = RankingViewType.ITEM
                                    )
                                }
                        }
                    }
                }
            }
        )
    }

    private fun getWeeklyQuestionTopic(): QuestionTopic {
        return getWeeklyQuestionTopicUseCase.invoke(
            GetWeeklyQuestionTopicUseCase.Params(
                getQuestionTopics()
            )
        )
    }

    private fun getQuestionTopics(): List<QuestionTopic> {
        return getQuestionTopicsUseCase.invoke()
    }
}