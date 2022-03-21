package com.etsisi.appquitectura.presentation.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.data.workers.QuestionsWorker
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginWithCredentialsUseCase
import com.etsisi.appquitectura.presentation.common.BaseAndroidViewModel
import kotlinx.coroutines.launch

class MainViewModel(
    applicationContext: Application,
    firebaseLoginWithCredentialsUseCase: FirebaseLoginWithCredentialsUseCase
): BaseAndroidViewModel(applicationContext, firebaseLoginWithCredentialsUseCase) {
    init {
        viewModelScope.launch {
            QuestionsWorker.fetchAllQuestions(applicationContext)
        }
    }
}