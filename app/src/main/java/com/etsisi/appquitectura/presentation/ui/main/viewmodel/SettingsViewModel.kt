package com.etsisi.appquitectura.presentation.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.LogOutUseCase
import com.etsisi.appquitectura.domain.usecase.SendEmailVerificationUseCase
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.BaseLoginViewModel
import com.etsisi.appquitectura.presentation.ui.main.model.ItemSettings
import com.etsisi.appquitectura.presentation.ui.main.model.ItemSettingsAction

class SettingsViewModel(
    firebaseLoginWithCredentialsUseCase: FirebaseLoginWithCredentialsUseCase,
    sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    logOutUseCase: LogOutUseCase
): BaseLoginViewModel(logOutUseCase, firebaseLoginWithCredentialsUseCase, sendEmailVerificationUseCase){

    val _sections = MutableLiveData<List<ItemSettings>>()
    val sections: LiveData<List<ItemSettings>>
        get() = _sections

    private val _onLogOut = MutableLiveEvent<Boolean>()
    val onLogOut: LiveEvent<Boolean>
    get() = _onLogOut

    init {
        _sections.value = listOf(
            ItemSettings(R.string.item_settings_log_out, R.drawable.ic_settings, ItemSettingsAction.LOG_OUT, true)
        )
    }

    fun handleSettings(item: ItemSettings) {
        when(item.action) {
            ItemSettingsAction.LOG_OUT -> {
                logOut {
                    _onLogOut.value = Event(true)
                }
            }
        }
    }

}