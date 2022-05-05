package com.etsisi.appquitectura.presentation.ui.main.ranking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.domain.usecase.FetchRankingUseCase
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
                    _ranking.value = it.mapIndexed { index, userBO -> ItemRanking(userBO, index+1) }
                }
            }
        )
    }
}