package com.etsisi.appquitectura.presentation.ui.main.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.presentation.ui.main.home.model.ItemHome
import com.etsisi.appquitectura.presentation.ui.main.home.model.ItemHomeAction

class HomeViewModel: ViewModel(){

    private val _sections = MutableLiveData<List<ItemHome>>()
    val sections: LiveData<List<ItemHome>>
        get() = _sections


    init {
        _sections.value = listOf(
            ItemHome(R.string.item_home_profile, R.raw.lottie_profile, ItemHomeAction.PROFILE),
            ItemHome(R.string.item_home_play, R.raw.lottie_play, ItemHomeAction.START_GAME),
            ItemHome(R.string.item_home_ranking, R.raw.lottie_ranking, ItemHomeAction.RANKING),
            ItemHome(R.string.item_home_configuration, R.raw.lottie_settings, ItemHomeAction.CONFIGURATION),
            ItemHome(R.string.item_home_analytics, R.raw.lottie_settings, ItemHomeAction.ANALYTICS),
            ItemHome(R.string.item_home_about, R.raw.lottie_settings, ItemHomeAction.ABOUT)
        )
    }
}