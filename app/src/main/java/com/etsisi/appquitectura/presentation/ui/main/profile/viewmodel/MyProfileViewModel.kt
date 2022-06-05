package com.etsisi.appquitectura.presentation.ui.main.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.etsisi.appquitectura.data.model.enums.ScoreLevel
import com.etsisi.appquitectura.domain.model.UserBO
import com.etsisi.appquitectura.domain.usecase.FetchScoresReferenceUseCase
import com.etsisi.appquitectura.domain.usecase.FetchUserProfileUseCase

class MyProfileViewModel(
    private val fetchUserProfileUseCase: FetchUserProfileUseCase,
    private val fetchScoresReferenceUseCase: FetchScoresReferenceUseCase
) : ViewModel() {
    private val _currentUser = MutableLiveData<UserBO>()
    val currentUser: LiveData<UserBO>
        get() = _currentUser

    private val _userLevel = MutableLiveData<ScoreLevel>()
    val userLevel: LiveData<ScoreLevel>
        get() = _userLevel

    init {
        fetchUserProfileUseCase.invoke(
            scope = viewModelScope,
            params = Unit,
            onResult = {
                it?.let { user ->
                    _currentUser.value = user

                    fetchScoresReferenceUseCase.invoke(
                        scope = viewModelScope,
                        params = Unit,
                        onResult = { references ->
                            if (references.isNotEmpty()) {
                                _userLevel.value = references.find { score ->
                                    user.gameExperience >= score.value
                                }?.let { it.level } ?:ScoreLevel.LEVEL_0
                            }
                        }
                    )
                }
            }
        )
    }
}