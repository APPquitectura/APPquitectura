package com.etsisi.appquitectura.presentation.ui.main.settings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.data.helper.PreferencesHelper
import com.etsisi.appquitectura.data.model.enums.PreferenceKeys
import com.etsisi.appquitectura.domain.usecase.SignInWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.LogOutUseCase
import com.etsisi.appquitectura.domain.usecase.SendEmailVerificationUseCase
import com.etsisi.appquitectura.domain.usecase.UpdateQuestionsUseCase
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.BaseLoginViewModel
import com.etsisi.appquitectura.presentation.ui.main.settings.model.ItemSettings
import com.etsisi.appquitectura.presentation.ui.main.settings.model.ItemSettingsAction

class SettingsViewModel(
    private val updateQuestionsUseCase: UpdateQuestionsUseCase,
    signInWithCredentialsUseCase: SignInWithCredentialsUseCase,
    sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    logOutUseCase: LogOutUseCase
) : BaseLoginViewModel(logOutUseCase, signInWithCredentialsUseCase, sendEmailVerificationUseCase) {

    private val _sections = MutableLiveData<List<ItemSettings>>()
    val sections: LiveData<List<ItemSettings>>
        get() = _sections

    private val _onLogOut = MutableLiveEvent<Boolean>()
    val onLogOut: LiveEvent<Boolean>
        get() = _onLogOut

    init {
        _sections.value = listOf(
                ItemSettings(R.string.item_settings_log_out, R.drawable.ic_settings, ItemSettingsAction.LOG_OUT, true),
                ItemSettings(R.string.item_settings_update_questions, R.drawable.ic_settings, ItemSettingsAction.UPDATE_QUESTIONS, false),
                ItemSettings(R.string.item_settings_repeat_mode, R.drawable.ic_settings, ItemSettingsAction.ENABLE_REPEATING_MODE, false)
        )
    }

    fun handleSettings(item: ItemSettings) {
        when (item.action) {
            ItemSettingsAction.LOG_OUT -> {
                logOut {
                    _onLogOut.value = Event(true)
                }
            }
            ItemSettingsAction.UPDATE_QUESTIONS -> {
                updateQuestions()
            }
            ItemSettingsAction.ENABLE_REPEATING_MODE -> {

            }
        }
    }

    private fun updateQuestions() {
        updateQuestionsUseCase.invoke(
                params = Unit
        ) {

        }
    }

    fun isRepeatModeEnabled() = PreferencesHelper.read(PreferenceKeys.REPEAT_WRONG_QUESTIONS, false)

    fun enableDisableRepeatMode(enabled: Boolean) = PreferencesHelper.write(PreferenceKeys.REPEAT_WRONG_QUESTIONS, enabled)

}