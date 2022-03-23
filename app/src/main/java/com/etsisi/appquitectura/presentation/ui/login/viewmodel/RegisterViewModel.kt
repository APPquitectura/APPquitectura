package com.etsisi.appquitectura.presentation.ui.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.usecase.CheckUserIsRegisteredUseCase
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.domain.usecase.SendEmailVerificationUseCase
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent
import com.etsisi.appquitectura.presentation.dialog.model.DialogConfig
import com.etsisi.appquitectura.presentation.ui.login.enums.RegisterError

class RegisterViewModel(
    firebaseLoginWithCredentialsUseCase: FirebaseLoginWithCredentialsUseCase,
    sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val registerUseCase: RegisterUseCase
) : BaseLoginViewModel(
    firebaseLoginWithCredentialsUseCase,
    sendEmailVerificationUseCase
) {

    private val _loaded by lazy { MutableLiveData<Boolean>() }
    val loaded: LiveData<Boolean>
        get() = _loaded

    private val _onSuccessRegister by lazy { MutableLiveEvent<Boolean>() }
    val onSuccessRegister: LiveEvent<Boolean>
        get() = _onSuccessRegister

    fun initRegister() {
        val email = _email.value.orEmpty()
        val password = _password.value.orEmpty()
        if (emailValid(email) && passwordValid(password)) {
            registerUseCase.invoke(
                scope = viewModelScope,
                params = RegisterUseCase.Params(email, password)
            ) { resultCode ->
                when (resultCode) {
                    RegisterUseCase.RESULT_CODES.WEAK_PASSWORD -> {
                        val config = DialogConfig(
                            title = R.string.generic_error_title,
                            body = R.string.error_weak_password,
                            lottieRes = R.raw.lottie_404
                        )
                        _onError.value = Event(config)
                    }
                    RegisterUseCase.RESULT_CODES.EMAIL_ALREADY_EXISTS -> {
                        val config = DialogConfig(
                            title = R.string.generic_error_title,
                            body = R.string.error_email_already_exists,
                            lottieRes = R.raw.lottie_404
                        )
                        _onError.value = Event(config)
                    }
                    RegisterUseCase.RESULT_CODES.EMAIL_MALFORMED -> {
                        val config = DialogConfig(
                            title = R.string.generic_error_title,
                            body = R.string.error_email_malformed,
                            lottieRes = R.raw.lottie_404
                        )
                        _onError.value = Event(config)
                    }
                    RegisterUseCase.RESULT_CODES.GENERIC_ERROR -> {
                        val config = DialogConfig(
                            title = R.string.generic_error_title,
                            body = R.string.generic_error_body,
                            lottieRes = R.raw.lottie_404
                        )
                        _onError.value = Event(config)
                    }
                    RegisterUseCase.RESULT_CODES.DATABASE_ERROR -> {
                        val config = DialogConfig(
                            title = R.string.generic_error_title,
                            body = R.string.error_register_database,
                            lottieRes = R.raw.message_alert
                        )
                        _onError.value = Event(config)
                    }
                    RegisterUseCase.RESULT_CODES.SUCCESS -> onSuccessRegister()
                }
            }
        } else {
            val config = DialogConfig(
                title = R.string.generic_error_title,
                body = R.string.error_email_password_malformed,
                lottieRes = R.raw.lottie_404
            )
            _onError.value = Event(config)
        }
    }

    private fun emailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun passwordValid(password: String) = password.length > 6

    fun onSuccessRegister() {
        if (CurrentUser.isEmailVerfied) {
            _onSuccessRegister.value = Event(true)
        } else {
            initVerifyEmail()
        }
    }

    fun setError(errorCode: Int) {
        when (errorCode) {
            RegisterError.YEAR_UNSELECTED.value -> {
                _onError.value = Event(
                    DialogConfig(
                        title = R.string.generic_error_title,
                        body = R.string.error_unselected_year,
                        lottieRes = R.raw.lottie_404
                    )
                )
            }
        }
    }
}