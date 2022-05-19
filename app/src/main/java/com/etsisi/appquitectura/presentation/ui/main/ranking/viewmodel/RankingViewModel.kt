package com.etsisi.appquitectura.presentation.ui.main.ranking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.domain.usecase.FetchRankingUseCase
import com.etsisi.appquitectura.presentation.ui.main.ranking.adapter.RankingViewType
import com.etsisi.appquitectura.presentation.ui.main.ranking.model.ItemRanking

class RankingViewModel(
    private val fetchRankingUseCase: FetchRankingUseCase
): ViewModel() {

    private val _ranking = MutableLiveData<List<ItemRanking>>()
    val ranking: LiveData<List<ItemRanking>>
        get() = _ranking

    init {
        getRanking()
    }

    fun getRanking() {
        fetchRankingUseCase.invoke(
            scope = viewModelScope,
            params = Unit,
            onResult = {
                if (it.isNotEmpty()) {

                    _ranking.value = buildList {
                        add(0,ItemRanking(
                            position = 1,
                            rankingPoints = emptyMap(),
                            name = "a",
                            viewType = RankingViewType.HEADER
                        ))
                        /*it.mapIndexed { index, rankingBO ->
                            add(ItemRanking(
                                position = index + 1,
                                rankingPoints = rankingBO.rankingPoints,
                                name = rankingBO.user?.name.orEmpty(),
                                viewType = RankingViewType.ITEM
                            ))
                        }*/

                        for (i in 1..10) {
                            add(0,ItemRanking(
                                position = i,
                                rankingPoints = it[0].rankingPoints,
                                name = it[0].user?.name.orEmpty(),
                                viewType = RankingViewType.ITEM
                            ))
                        }

                        add(0,ItemRanking(
                            position = 1,
                            rankingPoints = emptyMap(),
                            name = "a",
                            viewType = RankingViewType.HEADER
                        ))

                        for (i in 1..10) {
                            add(0,ItemRanking(
                                position = i,
                                rankingPoints = it[0].rankingPoints,
                                name = it[0].user?.name.orEmpty(),
                                viewType = RankingViewType.ITEM
                            ))
                        }
                    }
                }
            }
        )
    }
}