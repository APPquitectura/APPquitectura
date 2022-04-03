package com.etsisi.appquitectura.presentation.ui.login.viewmodel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.usecase.SignInWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.LogOutUseCase
import com.etsisi.appquitectura.domain.usecase.SendEmailVerificationUseCase
import com.etsisi.appquitectura.presentation.common.BaseViewModel
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent
import com.etsisi.appquitectura.presentation.dialog.model.DialogConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

open class BaseLoginViewModel(
    private val logOutUseCase: LogOutUseCase,
    private val signInWithCredentialsUseCase: SignInWithCredentialsUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase
    ): BaseViewModel(), LifecycleObserver {

    protected val _email by lazy { MutableLiveData<String>() }
    val email: MutableLiveData<String>
        get() = _email

    protected val _password by lazy { MutableLiveData<String>() }
    val password: MutableLiveData<String>
        get() = _password

    protected val _onSuccessLogin by lazy { MutableLiveEvent<Boolean>() }
    val onSuccessLogin: LiveEvent<Boolean>
        get() = _onSuccessLogin

    private val _emailVerificationSended by lazy { MutableLiveEvent<Boolean>() }
    val emailVerificationSended: LiveEvent<Boolean>
        get() = _emailVerificationSended

    protected val _onError = MutableLiveEvent<DialogConfig>()
    val onError: LiveEvent<DialogConfig>
        get() = _onError

    lateinit var googleClient: GoogleSignInClient
        private set

    fun initFirebaseLoginWithCredentials(account: GoogleSignInAccount, context: AppCompatActivity) {
        if (account.idToken != null) {
            showLoading(true, R.string.loading_sign_in_google)
            signInWithCredentialsUseCase.invoke(
                params = SignInWithCredentialsUseCase.Params(account.idToken!!, context, account.email.orEmpty())
            ) { resultCode ->
                showLoading(false)
                _onSuccessLogin.value = Event(resultCode == SignInWithCredentialsUseCase.RESULT_CODES.SUCESS)
                when(resultCode) {
                    SignInWithCredentialsUseCase.RESULT_CODES.COLLISION -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_sign_in_google_collision, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    SignInWithCredentialsUseCase.RESULT_CODES.CREDENTIALS_MALFORMED -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.generic_error_body, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    SignInWithCredentialsUseCase.RESULT_CODES.INVALID_USER -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_sign_in_google_user_not_exists, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    SignInWithCredentialsUseCase.RESULT_CODES.DATABASE_ERROR -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_register_database, lottieRes = R.raw.message_alert)
                        _onError.value = Event(config)
                    }
                }
            }
        } else {
            _onSuccessLogin.value = Event(false)
        }
    }


    fun initSilentLogin(context: AppCompatActivity): Boolean {
        return if (!CurrentUser.isSigned()) {
            GoogleSignIn.getLastSignedInAccount(context)?.let { account ->
                initFirebaseLoginWithCredentials(account, context)
                true
            } ?: run {
                _onSuccessLogin.value = Event(false)
                true
            }
        } else {
            true
        }
    }

    fun initVerifyEmail() {
        sendEmailVerificationUseCase.invoke(
            scope = viewModelScope,
            params = SendEmailVerificationUseCase.Params()
        ) { resultCodes ->
            showLoading(false)
            if (resultCodes == SendEmailVerificationUseCase.RESULT_CODES.SUCESS) {
                _emailVerificationSended.value = Event(true)
            } else {
                val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_sending_code_verification, lottieRes = R.raw.lottie_404)
                _onError.value = Event(config)
            }
        }
    }

    fun logOut(callback: () -> Unit) {
        logOutUseCase.invoke(
            params = LogOutUseCase.Params(googleClient),
            onResult = {
                callback()
            }
        )
    }

    fun setGoogleClient(context: Context, token: String) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(token)
            .build()

        googleClient = GoogleSignIn.getClient(context, gso)
    }
}