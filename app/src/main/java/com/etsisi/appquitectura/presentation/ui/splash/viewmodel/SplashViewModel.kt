package com.etsisi.appquitectura.presentation.ui.splash.viewmodel

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.usecase.CheckVerificationCodeUseCase
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginWithCredentialsUseCase
import com.etsisi.appquitectura.presentation.common.BaseAndroidViewModel
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.dynamiclinks.PendingDynamicLinkData

class SplashViewModel(
    applicationContext: Application,
    firebaseLoginWithCredentialsUseCase: FirebaseLoginWithCredentialsUseCase,
    private val checkVerificationCodeUseCase: CheckVerificationCodeUseCase
): BaseAndroidViewModel(applicationContext, firebaseLoginWithCredentialsUseCase) {

    private val _onCodeVerified by lazy { MutableLiveEvent<Boolean>() }
    val onCodeVerified: LiveEvent<Boolean>
        get() = _onCodeVerified

    private val _successLogin = MediatorLiveData<Event<Boolean>>()
    val successLogin: MediatorLiveData<Event<Boolean>>
        get() = _successLogin

    init {
        _successLogin.addSource(_onSuccessLogin) {
            _successLogin.value = it
        }
        _successLogin.addSource(_onError) {
            _successLogin.value = Event(false)
        }
    }

    fun initVerificationCode(pendingDynamicLinkData: PendingDynamicLinkData?) {
        if (pendingDynamicLinkData != null) {
            checkVerificationCodeUseCase.invoke(
                scope = viewModelScope,
                params = CheckVerificationCodeUseCase.Params(pendingDynamicLinkData)
            ) { resultCode ->
                _onCodeVerified.value = Event(resultCode == CheckVerificationCodeUseCase.RESULT_CODES.SUCESS)
            }
        }
    }

    fun login(context: AppCompatActivity) {
        if (!CurrentUser.isSigned()) {
            GoogleSignIn.getLastSignedInAccount(context)?.let { account ->
                initFirebaseLoginWithCredentials(account, false, context)
            } ?: run {
                _onSuccessLogin.value = Event(CurrentUser.isSigned())
            }
        } else {
            _onSuccessLogin.value = Event(true)
        }
    }
}