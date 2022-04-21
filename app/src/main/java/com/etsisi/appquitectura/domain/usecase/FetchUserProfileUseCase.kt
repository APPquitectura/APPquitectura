package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.model.UserBO

class FetchUserProfileUseCase(
    private val repository: UsersRepository
): UseCase<Unit, UserBO?>() {
    override suspend fun run(params: Unit): UserBO? {
        return CurrentUser.email?.let { repository.getUserById(it) }
    }
}