package com.etsisi.appquitectura.presentation.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.presentation.ui.main.model.ItemHome
import com.etsisi.appquitectura.presentation.ui.main.model.ItemHomeAction

class HomeViewModel: ViewModel(){

    private val _sections = MutableLiveData<List<ItemHome>>()
    val sections: LiveData<List<ItemHome>>
        get() = _sections


    init {
        _sections.value = listOf(
            ItemHome(R.string.item_home_profile, R.drawable.ic_profile, ItemHomeAction.PROFILE),
            ItemHome(R.string.item_home_play, R.drawable.ic_play, ItemHomeAction.START_GAME),
            ItemHome(R.string.item_home_ranking, R.drawable.ic_ranking, ItemHomeAction.RANKING),
            ItemHome(R.string.item_home_configuration, R.drawable.ic_settings, ItemHomeAction.CONFIGURATION),
            ItemHome(R.string.item_home_analytics, R.drawable.ic_analytics, ItemHomeAction.ANALYTICS),
            ItemHome(R.string.item_home_about, R.drawable.ic_about, ItemHomeAction.ABOUT)
        )
    }
}