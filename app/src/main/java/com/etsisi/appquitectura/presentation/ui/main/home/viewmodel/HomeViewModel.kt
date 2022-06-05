package com.etsisi.appquitectura.presentation.ui.main.home.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.etsisi.analytics.IFirebaseAnalytics
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent
import com.etsisi.appquitectura.presentation.ui.main.home.model.ItemHome
import com.etsisi.appquitectura.presentation.ui.main.home.model.ItemHomeAction

class HomeViewModel(
    private val analytics: IFirebaseAnalytics
): ViewModel(){

    private val _sections = MutableLiveData<List<ItemHome>>()
    val sections: LiveData<List<ItemHome>>
        get() = _sections

    private val _onMenuItemClicked = MutableLiveEvent<ItemHomeAction>()
    val onMenuItemClicked: LiveEvent<ItemHomeAction>
        get() = _onMenuItemClicked


    init {
        _sections.value = listOf(
            ItemHome(R.string.item_home_play, R.raw.lottie_play, ItemHomeAction.START_GAME),
            ItemHome(R.string.item_home_ranking, R.raw.lottie_ranking, ItemHomeAction.RANKING),
            ItemHome(R.string.item_home_profile, R.raw.lottie_profile, ItemHomeAction.PROFILE)
            )
    }

    fun handleMenuItemSelected(item: ItemHome, context: Context) {
        with(context){
            analytics.onItemHomeMenuClick(itemId = getString(item.title), contentType = item.action.name)
            _onMenuItemClicked.value = Event(item.action)
        }
    }
}