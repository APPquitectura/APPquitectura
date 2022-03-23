package com.etsisi.appquitectura.presentation.ui.login.viewmodel

import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.usecase.CheckUserIsRegisteredUseCase
import com.etsisi.appquitectura.domain.usecase.CheckVerificationCodeUseCase
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginUseCase
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.SendEmailVerificationUseCase
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent
import com.etsisi.appquitectura.presentation.dialog.model.DialogConfig
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.dynamiclinks.PendingDynamicLinkData

class LoginViewModel(
    firebaseLoginWithCredentialsUseCase: FirebaseLoginWithCredentialsUseCase,
    sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val checkUserIsRegisteredUseCase: CheckUserIsRegisteredUseCase,
    private val firebaseLoginUseCase: FirebaseLoginUseCase,
    private val checkVerificationCodeUseCase: CheckVerificationCodeUseCase
): BaseLoginViewModel(firebaseLoginWithCredentialsUseCase, sendEmailVerificationUseCase) {

    private val _loaded by lazy { MutableLiveData<Boolean>() }
    val loaded: LiveData<Boolean>
        get() = _loaded

    private val _onCodeVerified by lazy { MutableLiveEvent<Boolean>() }
    val onCodeVerified: LiveEvent<Boolean>
        get() = _onCodeVerified

    private val _onRegister = MutableLiveEvent<Boolean>()
    val onRegister: LiveEvent<Boolean>
        get() = _onRegister

    fun onGoogleSignInClicked(account: GoogleSignInAccount, context: AppCompatActivity) {
        checkUserIsRegisteredUseCase.invoke(
            scope = viewModelScope,
            params = CheckUserIsRegisteredUseCase.Params(account.idToken.orEmpty())
        ) { userExists ->
            if (userExists) {
                initFirebaseLoginWithCredentials(account, context)
            } else {
                val config = DialogConfig(title = R.string.error_login_credentials_title, body = R.string.error_sign_in_google_user_not_exists, lottieRes = R.raw.lottie_404)
                _onError.value = Event(config)
            }
        }
    }

    fun initFirebaseLogin() {
        val email = _email.value?.trim().orEmpty()
        val password = _password.value?.trim().orEmpty()
        if (emailValid(email) && !password.isBlank()) {
            _loaded.value = false
            firebaseLoginUseCase.invoke(
                scope = viewModelScope,
                params = FirebaseLoginUseCase.Params(email, password)
            ) { resultCode ->
                _loaded.value = true
                when(resultCode) {
                    FirebaseLoginUseCase.RESULT_CODES.PASSWORD_INVALID -> {
                        val config = DialogConfig(title = R.string.error_login_credentials_title, body = R.string.error_login_credentials_body, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    FirebaseLoginUseCase.RESULT_CODES.EMAIL_INVALID -> {
                        val config = DialogConfig(title = R.string.error_login_credentials_title, body = R.string.error_sign_in_google_user_not_exists, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    FirebaseLoginUseCase.RESULT_CODES.GENERIC_ERROR -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.generic_error_body, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    FirebaseLoginUseCase.RESULT_CODES.SUCCESS -> onSuccessLogin()
                }
            }
        } else {
            val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_login_credentials_body, lottieRes = R.raw.lottie_404)
            _onError.value = Event(config)
        }
    }

    fun initGoogleLoginFailed(statusCode: Int) {
        when(statusCode) {
            CommonStatusCodes.SIGN_IN_REQUIRED -> {
                val config = DialogConfig(title = R.string.error_sign_in_required_google_title, body = R.string.error_sign_in_required_google_body, lottieRes = R.raw.lottie_404)
                _onError.value = Event(config)
            }
            CommonStatusCodes.NETWORK_ERROR -> {
                val config = DialogConfig(title = R.string.error_network_title, body = R.string.error_network_body, lottieRes = R.raw.lottie_404)
                _onError.value = Event(config)
            }
            CommonStatusCodes.INVALID_ACCOUNT,
            CommonStatusCodes.INTERNAL_ERROR -> {
                val config = DialogConfig(title = R.string.error_internal_google_title, body = R.string.error_internal_google_body, lottieRes = R.raw.lottie_404)
                _onError.value = Event(config)
            }
            GoogleSignInStatusCodes.SIGN_IN_FAILED -> {
                val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_sign_in_google_user_not_exists, lottieRes = R.raw.lottie_404)
                _onError.value = Event(config)
            }
            GoogleSignInStatusCodes.SIGN_IN_CURRENTLY_IN_PROGRESS -> {
                val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_sign_in_google_in_progress, lottieRes = R.raw.lottie_404)
                _onError.value = Event(config)
            }
        }
    }

    fun onRegister() {
        _onRegister.value = Event(true)
    }

    fun initVerificationCode(pendingDynamicLinkData: PendingDynamicLinkData?) {
        if (pendingDynamicLinkData != null) {
            showLoading(true, R.string.loading_veryfing_code)
            checkVerificationCodeUseCase.invoke(
                scope = viewModelScope,
                params = CheckVerificationCodeUseCase.Params(pendingDynamicLinkData)
            ) { resultCode ->
                showLoading(false)
                _onCodeVerified.value = Event(resultCode == CheckVerificationCodeUseCase.RESULT_CODES.SUCESS)
            }
        }
    }

    fun onSuccessLogin() {
        _loaded.value = true
        if (CurrentUser.isEmailVerfied) {
            _onSuccessLogin.value = Event(true)
        } else {
            showLoading(true)
            initVerifyEmail()
        }
    }

    private fun emailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()



}