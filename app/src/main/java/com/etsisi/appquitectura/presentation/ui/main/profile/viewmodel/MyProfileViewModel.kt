package com.etsisi.appquitectura.presentation.ui.main.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.domain.model.UserBO
import com.etsisi.appquitectura.domain.usecase.FetchUserProfileUseCase

class MyProfileViewModel(
    private val fetchUserProfileUseCase: FetchUserProfileUseCase
): ViewModel() {
    private val _currentUser = MutableLiveData<UserBO>()
    val currentUser: LiveData<UserBO>
        get() = _currentUser

    init {
        fetchUserProfileUseCase.invoke(
            scope = viewModelScope,
            params = Unit,
            onResult = { userBO ->
            userBO?.let { _currentUser.value = it }
            }
        )
    }
}