package com.etsisi.appquitectura.presentation.common

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.presentation.dialog.model.DialogConfig
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

open class BaseAndroidViewModel(
    protected val applicationContext: Application,
    private val firebaseLoginWithCredentialsUseCase: FirebaseLoginWithCredentialsUseCase
    ): AndroidViewModel(applicationContext), LifecycleObserver {

    protected val _loading by lazy { MutableLiveData<Pair<Boolean, String?>>() }
    val loading: LiveData<Pair<Boolean, String?>>
        get() = _loading

    protected val _onSuccessLogin by lazy { MutableLiveEvent<Boolean>() }
    val onSuccessLogin: LiveEvent<Boolean>
        get() = _onSuccessLogin

    protected val _onError = MutableLiveEvent<DialogConfig>()
    val onError: LiveEvent<DialogConfig>
        get() = _onError

    fun showLoading(show: Boolean, msgRes: Int? = null) {
        val msg = msgRes?.let { applicationContext.getString(it) }
        _loading.value = Pair(show, msg)
    }

    fun initFirebaseLoginWithCredentials(account: GoogleSignInAccount, createUser: Boolean, context: AppCompatActivity) {
        if (account.idToken != null) {
            showLoading(true, R.string.loading_sign_in_google)
            firebaseLoginWithCredentialsUseCase.invoke(
                params = FirebaseLoginWithCredentialsUseCase.Params(account.idToken!!, context, createUser, account.email.orEmpty())
            ) { resultCode ->
                when(resultCode) {
                    FirebaseLoginWithCredentialsUseCase.RESULT_CODES.SUCESS -> {
                        _onSuccessLogin.value = Event(true)
                    }
                    FirebaseLoginWithCredentialsUseCase.RESULT_CODES.COLLISION -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_sign_in_google_collision, lottieRes = R.raw.lottie_404)
                        showLoading(false)
                        _onError.value = Event(config)
                    }
                    FirebaseLoginWithCredentialsUseCase.RESULT_CODES.CREDENTIALS_MALFORMED -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.generic_error_body, lottieRes = R.raw.lottie_404)
                        showLoading(false)
                        _onError.value = Event(config)
                    }
                    FirebaseLoginWithCredentialsUseCase.RESULT_CODES.INVALID_USER -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_sign_in_google_user_not_exists, lottieRes = R.raw.lottie_404)
                        showLoading(false)
                        _onError.value = Event(config)
                    }
                    FirebaseLoginWithCredentialsUseCase.RESULT_CODES.DATABASE_ERROR -> {
                        val config = DialogConfig(title = R.string.generic_error_title, body = R.string.error_register_database, lottieRes = R.raw.message_alert)
                        showLoading(false)
                        _onError.value = Event(config)
                    }
                }
            }
        }
    }
}