package com.etsisi.appquitectura.presentation.ui.login.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
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
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.domain.usecase.SendEmailVerificationUseCase
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent
import com.etsisi.appquitectura.presentation.dialog.model.DialogConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.dynamiclinks.PendingDynamicLinkData

class LoginViewModel(
    private val applicationContext: Application,
    private val registerUseCase: RegisterUseCase,
    private val firebaseLoginUseCase: FirebaseLoginUseCase,
    private val firebaseLoginWithCredentialsUseCase: FirebaseLoginWithCredentialsUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val checkVerificationCodeUseCase: CheckVerificationCodeUseCase
): AndroidViewModel(applicationContext), LifecycleObserver {

    private val _loaded by lazy { MutableLiveData<Boolean>() }
    val loaded: LiveData<Boolean>
        get() = _loaded

    private val _email by lazy { MutableLiveData<String>() }
    val email: MutableLiveData<String>
        get() = _email

    private val _password by lazy { MutableLiveData<String>() }
    val password: MutableLiveData<String>
        get() = _password

    private val _onRegister = MutableLiveEvent<Boolean>()
    val onRegister: LiveEvent<Boolean>
        get() = _onRegister

    private val _onError = MutableLiveEvent<DialogConfig>()
    val onError: LiveEvent<DialogConfig>
        get() = _onError

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

    lateinit var googleClient: GoogleSignInClient
        private set

    fun isUserLogged(): Boolean = CurrentUser.instance != null

    fun initFirebaseLogin() {
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
                    FirebaseLoginUseCase.RESULT_CODES.PASSWORD_INVALID,
                    FirebaseLoginUseCase.RESULT_CODES.EMAIL_INVALID -> {
                        val config = DialogConfig(title = R.string.error_login_credentials_title, body = R.string.error_login_credentials_body, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    FirebaseLoginUseCase.RESULT_CODES.GENERIC_ERROR -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.generic_error_body, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    FirebaseLoginUseCase.RESULT_CODES.SUCCESS -> _onSuccessLogin.value = Event(true)
                }
            }
        } else {
            val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_login_credentials_body, lottieRes = R.raw.lottie_404)
            _onError.value = Event(config)
        }
    }

    fun initFirebaseLoginWithCredentials(token: String?, context: AppCompatActivity) {
        if (token != null) {
            firebaseLoginWithCredentialsUseCase.invoke(
                scope = viewModelScope,
                params = FirebaseLoginWithCredentialsUseCase.Params(token, context)
            ) { resultCode ->
                when(resultCode) {
                    FirebaseLoginWithCredentialsUseCase.RESULT_CODES.SUCESS -> _onSuccessLogin.value = Event(true)
                    FirebaseLoginWithCredentialsUseCase.RESULT_CODES.COLLISION -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_sign_in_google_collision, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    FirebaseLoginWithCredentialsUseCase.RESULT_CODES.CREDENTIALS_MALFORMED -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.generic_error_body, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    FirebaseLoginWithCredentialsUseCase.RESULT_CODES.INVALID_USER -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_sign_in_google_user_not_exists, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                }
            }
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
        }
    }

    fun onRegister() {
        _onRegister.value = Event(true)
    }

    fun initRegister() {
        val email = _email.value.orEmpty()
        val password = _password.value.orEmpty()
        if (emailValid(email) && passwordValid(password)) {
            showLoading(true)
            registerUseCase.invoke(
                scope = viewModelScope,
                params = RegisterUseCase.Params(email, password)
            ) { resultCode ->
                showLoading(false)
                when(resultCode) {
                    RegisterUseCase.RESULT_CODES.WEAK_PASSWORD -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_weak_password, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    RegisterUseCase.RESULT_CODES.EMAIL_ALREADY_EXISTS -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_email_already_exists, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    RegisterUseCase.RESULT_CODES.EMAIL_MALFORMED -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_email_malformed, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    RegisterUseCase.RESULT_CODES.GENERIC_ERROR -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.generic_error_body, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    RegisterUseCase.RESULT_CODES.SUCCESS -> onSuccessRegister()
                }
            }
        } else {
            val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_email_password_malformed, lottieRes = R.raw.lottie_404)
            _onError.value = Event(config)
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
                val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_sending_code_verification, lottieRes = R.raw.lottie_404)
                _onError.value = Event(config)
            }
        }
    }

    fun initVerificationCode(pendingDynamicLinkData: PendingDynamicLinkData?) {
        if (pendingDynamicLinkData != null) {
            checkVerificationCodeUseCase.invoke(
                scope = viewModelScope,
                params = CheckVerificationCodeUseCase.Params(pendingDynamicLinkData)
            ) { resultCode ->
                when(resultCode) {
                    CheckVerificationCodeUseCase.RESULT_CODES.GENERIC_ERROR -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_code_verification, lottieRes = R.raw.lottie_404)
                        _onError.value = Event(config)
                    }
                    CheckVerificationCodeUseCase.RESULT_CODES.SUCESS ->_onSuccessCode.value = Event(true)
                }
            }
        } else {
            val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_code_verification, lottieRes = R.raw.lottie_404)
            _onError.value = Event(config)
        }
    }

    private fun showLoading(flag: Boolean) {
        _loaded.value = flag.not()
    }

    private fun emailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun passwordValid(password: String) = password.length > 6

    fun btnDrawable(): Drawable = ContextCompat
        .getDrawable(applicationContext, R.drawable.ic_check_round_selected)
        ?.apply {
            setBounds(0, 0, 50, 50)
        }!!

    fun setGoogleClient(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleClient = GoogleSignIn.getClient(context, gso)
    }

}