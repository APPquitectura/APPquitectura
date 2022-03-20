package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.domain.model.CurrentUser

class LogOutUseCase: UseCase<Any, Unit>() {

    override suspend fun run(params: Any) {
        CurrentUser.signOut()
    }
}