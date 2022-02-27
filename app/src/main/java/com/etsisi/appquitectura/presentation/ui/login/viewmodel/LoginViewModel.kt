package com.etsisi.appquitectura.presentation.ui.login.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.etsisi.appquitectura.presentation.common.Event
import com.etsisi.appquitectura.presentation.common.LiveEvent
import com.etsisi.appquitectura.presentation.common.MutableLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel: ViewModel(), LifecycleObserver {
    lateinit var auth: FirebaseAuth

    private val _email by lazy { MutableLiveData<String>() }
    val email: MutableLiveData<String>
        get() = _email

    private val _password by lazy { MutableLiveData<String>() }
    val password: MutableLiveData<String>
        get() = _password

    private val _isUserLogged = MutableLiveEvent<Boolean>()
    val isUserLoggedIn: LiveEvent<Boolean>
        get() = _isUserLogged

    init {
        auth = Firebase.auth
        isUserLoggedIn()
    }

    fun isUserLoggedIn() {
        _isUserLogged.value = Event(auth != null)
    }

}