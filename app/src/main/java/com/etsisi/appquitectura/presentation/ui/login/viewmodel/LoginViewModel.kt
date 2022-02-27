package com.etsisi.appquitectura.presentation.ui.login.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent
import com.etsisi.appquitectura.presentation.utils.EMPTY

class LoginViewModel(
    private val applicationContext: Application,
    private val registerUseCase: RegisterUseCase,
    private val firebaseLoginUseCase: FirebaseLoginUseCase
): AndroidViewModel(applicationContext), LifecycleObserver {

    enum class REGISTER_NAVIGATION_TYPE {SUCESS, VERIFY_EMAIL}

    private val _loading by lazy { MutableLiveData<Boolean>() }
    val loading: LiveData<Boolean>
        get() = _loading

    private val _email by lazy { MutableLiveData<String>() }
    val email: MutableLiveData<String>
        get() = _email

    private val _password by lazy { MutableLiveData<String>() }
    val password: MutableLiveData<String>
        get() = _password

    private val _errorMsg by lazy { MutableLiveData<String>() }
    val errorMsg: MutableLiveData<String>
        get() = _errorMsg

    private val _isUserLogged = MutableLiveEvent<Boolean>()
    val isUserLoggedIn: LiveEvent<Boolean>
        get() = _isUserLogged

    private val _onRegister = MutableLiveEvent<Boolean>()
    val onRegister: LiveEvent<Boolean>
        get() = _onRegister

    private val _onSuccessRegister = MutableLiveEvent<REGISTER_NAVIGATION_TYPE>()
    val onSuccessRegister: LiveEvent<REGISTER_NAVIGATION_TYPE>
        get() = _onSuccessRegister

    private val _onSuccessLogin = MutableLiveEvent<Boolean>()
    val onSuccessLogin: LiveEvent<Boolean>
        get() = _onSuccessLogin

    init {
        isUserLoggedIn()
    }

    fun isUserLoggedIn() {
        _isUserLogged.value = Event(CurrentUser.currentUserInstance != null)
    }

    fun onRegister() {
        _email.value = String.EMPTY
        _password.value = String.EMPTY
        _onRegister.value = Event(true)
    }

    fun onSuccessRegister() {
        if (CurrentUser.isEmailVerfied) {
            CurrentUser.currentUserInstance?.sendEmailVerification()
            _onSuccessRegister.value = Event(REGISTER_NAVIGATION_TYPE.SUCESS)
        } else {
            _onSuccessRegister.value = Event(REGISTER_NAVIGATION_TYPE.VERIFY_EMAIL)
        }
    }

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

    private fun showLoading(flag: Boolean) {
        _loading.value = flag
    }

    private fun emailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

}