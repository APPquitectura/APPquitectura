package com.etsisi.appquitectura.presentation.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.data.workers.QuestionsWorker
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.LogOutUseCase
import com.etsisi.appquitectura.domain.usecase.SendEmailVerificationUseCase
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.BaseLoginViewModel
import kotlinx.coroutines.launch

class MainViewModel(
    applicationContext: Application,
    logOutUseCase: LogOutUseCase,
    firebaseLoginWithCredentialsUseCase: FirebaseLoginWithCredentialsUseCase,
    sendEmailVerificationUseCase: SendEmailVerificationUseCase
): BaseLoginViewModel(logOutUseCase, firebaseLoginWithCredentialsUseCase, sendEmailVerificationUseCase) {
    init {
        viewModelScope.launch {
            QuestionsWorker.fetchAllQuestions(applicationContext)
        }
    }
}