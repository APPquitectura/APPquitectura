package com.etsisi.appquitectura.presentation.ui.login.viewmodel

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Patterns
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.usecase.CheckVerificationCodeUseCase
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.domain.usecase.SendEmailVerificationUseCase
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent
import com.google.firebase.dynamiclinks.PendingDynamicLinkData

class LoginViewModel(
    private val applicationContext: Application,
    private val registerUseCase: RegisterUseCase,
    private val firebaseLoginUseCase: FirebaseLoginUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val checkVerificationCodeUseCase: CheckVerificationCodeUseCase
): AndroidViewModel(applicationContext), LifecycleObserver {

    private val _loaded by lazy { MutableLiveData<Boolean>() }
    val loaded: LiveData<Boolean>
        get() = _loaded

    private val _email by lazy { MutableLiveData<String>() }
    val email: MutableLiveData<String>
        get() = _email

    private val _verifyEmailMsg by lazy { MutableLiveData<String>(null) }
    val verifyEmailMsg: LiveData<String>
        get() = _verifyEmailMsg

    private val _password by lazy { MutableLiveData<String>() }
    val password: MutableLiveData<String>
        get() = _password

    private val _errorMsg by lazy { MutableLiveData<String>() }
    val errorMsg: MutableLiveData<String>
        get() = _errorMsg

    private val _isUserLogged = MutableLiveEvent(Event(CurrentUser.instance != null))
    val isUserLoggedIn: LiveEvent<Boolean>
        get() = _isUserLogged

    private val _onRegister = MutableLiveEvent<Boolean>()
    val onRegister: LiveEvent<Boolean>
        get() = _onRegister

    private val _onVerifyEmail by lazy { MutableLiveEvent<Boolean>() }
    val onVerifyEmail: LiveEvent<Boolean>
        get() = _onVerifyEmail

    private val _onSuccessRegister by lazy { MutableLiveEvent<Boolean>() }
    val onSuccessRegister: LiveEvent<Boolean>
        get() = _onSuccessRegister

    private val _onSuccessLogin by lazy { MutableLiveEvent<Boolean>() }
    val onSuccessLogin: LiveEvent<Boolean>
        get() = _onSuccessLogin

    private val _onSuccessCode by lazy { MutableLiveEvent<Boolean>() }
    val onSuccessCode: LiveEvent<Boolean>
        get() = _onSuccessCode

    fun initLogin() {
        val email = _email.value.orEmpty()
        val password = _password.value.orEmpty()
        if (emailValid(email) && !password.isBlank()) {
            showLoading(true)
            firebaseLoginUseCase.invoke(
                scope = viewModelScope,
                params = FirebaseLoginUseCase.Params(email, password)
            ) { resultCode ->
                showLoading(false)
                when(resultCode) {
                    FirebaseLoginUseCase.RESULT_CODES.EMAIL_INVALID,
                    FirebaseLoginUseCase.RESULT_CODES.PASSWORD_INVALID,
                    FirebaseLoginUseCase.RESULT_CODES.GENERIC_ERROR -> _errorMsg.value = applicationContext.getString(R.string.error_login_credentials)
                    FirebaseLoginUseCase.RESULT_CODES.SUCCESS -> _onSuccessLogin.value = Event(true)
                }
            }
        } else {
            _errorMsg.value = applicationContext.getString(R.string.error_text_input_empty)
        }
    }

    fun onRegister() {
        _onRegister.value = Event(true)
    }

    fun initRegister() {
        val email = _email.value.orEmpty()
        val password = _password.value.orEmpty()
        if (emailValid(email) && !password.isBlank()) {
            showLoading(true)
            registerUseCase.invoke(
                scope = viewModelScope,
                params = RegisterUseCase.Params(email, password)
            ) { resultCode ->
                showLoading(false)
                when(resultCode) {
                    RegisterUseCase.RESULT_CODES.WEAK_PASSWORD -> _errorMsg.value = applicationContext.getString(R.string.error_weak_password)
                    RegisterUseCase.RESULT_CODES.EMAIL_ALREADY_EXISTS -> _errorMsg.value = applicationContext.getString(R.string.error_email_already_exists)
                    RegisterUseCase.RESULT_CODES.EMAIL_MALFORMED -> _errorMsg.value = applicationContext.getString(R.string.error_email_malformed)
                    RegisterUseCase.RESULT_CODES.GENERIC_ERROR -> _errorMsg.value = applicationContext.getString(R.string.error_generic)
                    RegisterUseCase.RESULT_CODES.SUCCESS -> onSuccessRegister()
                }
            }
        } else {
            _errorMsg.value = applicationContext.getString(R.string.error_text_input_empty)
        }
    }

    fun onSuccessRegister() {
        if (CurrentUser.isEmailVerfied) {
            showLoading(false)
            _onSuccessRegister.value = Event(true)
        } else {
            initVerifyEmail()
        }
    }

    private fun initVerifyEmail() {
        sendEmailVerificationUseCase.invoke(
            scope = viewModelScope,
            params = SendEmailVerificationUseCase.Params()
        ) { resultCodes ->
            showLoading(false)
            if (resultCodes == SendEmailVerificationUseCase.RESULT_CODES.SUCESS) {
                _onVerifyEmail.value = Event(true)
            } else {
                _onSuccessRegister.value = Event(true)
            }
        }
    }

    fun initVerificationCode(pendingDynamicLinkData: PendingDynamicLinkData?) {
        if (pendingDynamicLinkData != null) {
            checkVerificationCodeUseCase.invoke(
                scope = viewModelScope,
                params = CheckVerificationCodeUseCase.Params(pendingDynamicLinkData)
            ) {
                _onSuccessCode.value = Event(it != CheckVerificationCodeUseCase.RESULT_CODES.GENERIC_ERROR)
            }
        } else {
            onCodeVerificationFailed()
        }
    }

    fun onCodeVerificationFailed() {
        _onSuccessCode.value = Event(false)
    }

    private fun showLoading(flag: Boolean) {
        _loaded.value = flag.not()
    }

    private fun emailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun btnDrawable(): Drawable = ContextCompat
        .getDrawable(applicationContext, R.drawable.ic_check_round_selected)
        ?.apply {
            setBounds(0, 0, 50, 50)
        }!!

}