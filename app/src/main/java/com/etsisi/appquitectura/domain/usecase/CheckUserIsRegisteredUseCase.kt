package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.UsersRepository

class CheckUserIsRegisteredUseCase(
        private val repository: UsersRepository
): UseCase<CheckUserIsRegisteredUseCase.Params, Boolean>() {

    data class Params(
        val email: String
    )

    enum class RESULT_CODES { NOT_EXISTS, GENERIC_ERROR, PERMISIONS_DENIED, SUCCESS }

    override suspend fun run(params: Params): Boolean {
        return repository.isUserRegistered(params.email) == RESULT_CODES.SUCCESS
    }
}