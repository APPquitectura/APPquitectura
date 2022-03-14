package com.etsisi.appquitectura.presentation.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.presentation.ui.main.model.ItemSettings
import com.etsisi.appquitectura.presentation.ui.main.model.ItemSettingsAction

class SettingsViewModel: ViewModel(){

    val _sections = MutableLiveData<List<ItemSettings>>()
    val sections: LiveData<List<ItemSettings>>
        get() = _sections

    init {
        _sections.value = listOf(
            ItemSettings(R.string.item_settings_log_out, R.drawable.ic_settings, ItemSettingsAction.LOG_OUT, true)
        )
    }

}