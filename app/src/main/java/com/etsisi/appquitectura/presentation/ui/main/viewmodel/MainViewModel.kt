package com.etsisi.appquitectura.presentation.ui.main.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginWithCredentialsUseCase
import com.etsisi.appquitectura.presentation.common.BaseAndroidViewModel
import com.etsisi.appquitectura.presentation.common.Event
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

class MainViewModel(
    applicationContext: Application,
    firebaseLoginWithCredentialsUseCase: FirebaseLoginWithCredentialsUseCase
): BaseAndroidViewModel(applicationContext, firebaseLoginWithCredentialsUseCase) {



}