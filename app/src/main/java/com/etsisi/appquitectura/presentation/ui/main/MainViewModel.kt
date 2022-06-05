package com.etsisi.appquitectura.presentation.ui.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.etsisi.analytics.IFirebaseAnalytics
import com.etsisi.appquitectura.data.workers.QuestionsWorker
import com.etsisi.appquitectura.domain.usecase.SignInWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.LogOutUseCase
import com.etsisi.appquitectura.domain.usecase.SendEmailVerificationUseCase
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.BaseLoginViewModel
import kotlinx.coroutines.launch

class MainViewModel(
    applicationContext: Application,
    analytics: IFirebaseAnalytics,
    logOutUseCase: LogOutUseCase,
    signInWithCredentialsUseCase: SignInWithCredentialsUseCase,
    sendEmailVerificationUseCase: SendEmailVerificationUseCase
): BaseLoginViewModel(analytics, logOutUseCase, signInWithCredentialsUseCase, sendEmailVerificationUseCase) {

    init {
        viewModelScope.launch {
            QuestionsWorker.fetchAllQuestions(applicationContext)
        }
    }
}