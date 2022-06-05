package com.etsisi.appquitectura.presentation.dialog.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.domain.usecase.ResetPasswordUseCase
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent

class InputTextViewModel(
        private val resetPassword: ResetPasswordUseCase
): ViewModel() {

    private val _input = MutableLiveData<String>()
    val input: MutableLiveData<String>
        get() = _input

    private val _inputError by lazy { MutableLiveData(false) }
    val inputError: MutableLiveData<Boolean>
        get() = _inputError

    private val _onPossitiveButtonClicked = MutableLiveEvent<Boolean>()
    val onPossitiveButtonClicked: LiveEvent<Boolean>
        get() = _onPossitiveButtonClicked

    private val _onResult by lazy { MutableLiveEvent<Boolean>() }
    val onResult: LiveEvent<Boolean>
        get() = _onResult

    fun onPositiveButtonClicked() {
        _onPossitiveButtonClicked.value = Event(true)
    }

    fun resetPassword() {
        _input.value?.let { input ->
            if (emailValid(input)) {
                resetPassword.invoke(
                        scope = viewModelScope,
                        params = ResetPasswordUseCase.Params(input)
                ) {
                    _onResult.value = Event(it == ResetPasswordUseCase.RESULT_CODES.SUCCESS)
                    _inputError.value = it == ResetPasswordUseCase.RESULT_CODES.ERROR
                }
            } else {
                _inputError.value = true
                _onResult.value = Event(false)
            }
        }
    }

    private fun emailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
}