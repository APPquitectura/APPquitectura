package com.etsisi.appquitectura.presentation.ui.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.domain.enums.QuestionSubject
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.usecase.SignInWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.LogOutUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.domain.usecase.SendEmailVerificationUseCase
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent
import com.etsisi.appquitectura.presentation.dialog.model.DialogConfig
import com.etsisi.appquitectura.presentation.ui.login.enums.RegisterError

class RegisterViewModel(
    logOutUseCase: LogOutUseCase,
    signInWithCredentialsUseCase: SignInWithCredentialsUseCase,
    sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val registerUseCase: RegisterUseCase
) : BaseLoginViewModel(
    logOutUseCase,
    signInWithCredentialsUseCase,
    sendEmailVerificationUseCase
) {

    private val _loaded by lazy { MutableLiveData<Boolean>() }
    val loaded: LiveData<Boolean>
        get() = _loaded

    private val _onSuccessRegister by lazy { MutableLiveEvent<Boolean>() }
    val onSuccessRegister: LiveEvent<Boolean>
        get() = _onSuccessRegister

    private val _name = MutableLiveData<String>()
    val name: MutableLiveData<String>
        get() = _name

    private val _surname = MutableLiveData<String>()
    val surname: MutableLiveData<String>
        get() = _surname

    private val _academicRecord = MutableLiveData<String>()
    val academicRecord: MutableLiveData<String>
        get() = _academicRecord

    private val _academicGroup = MutableLiveData<String>()
    val academicGroup: MutableLiveData<String>
        get() = _academicGroup

    private val _age = MutableLiveData<String>()
    val age: MutableLiveData<String>
        get() = _age

    private val _genre = MutableLiveData<String>()
    val genre: MutableLiveData<String>
        get() = _genre

    var spinnerOption: QuestionSubject? = null

    fun initRegister() {
        val email = _email.value.orEmpty()
        val password = _password.value.orEmpty()
        if (emailValid(email) && passwordValid(password) && spinnerOption != null && nameValid()) {
            registerUseCase.invoke(
                scope = viewModelScope,
                params = RegisterUseCase.Params(_name.value, email, password, spinnerOption!!)
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
    private fun nameValid() = _name.value?.isNotBlank() == true

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