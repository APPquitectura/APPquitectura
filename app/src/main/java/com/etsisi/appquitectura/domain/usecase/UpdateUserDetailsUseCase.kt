package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.domain.model.CurrentUser

class UpdateUserDetailsUseCase(
    private val repository: UsersRepository
): UseCase<UpdateUserDetailsUseCase.Params, UpdateUserDetailsUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { SUCCESS, FAILED }

    enum class USER_FIELD { PASSWORD, SCORE }

    data class Params (val fied: Pair<USER_FIELD, Any>)

    override suspend fun run(params: Params): RESULT_CODES {
        return if (!CurrentUser.email.isNullOrBlank()) {
            repository.getUserById(CurrentUser.email.orEmpty())?.let { userBO ->
                val newUser = when (params.fied.first) {
                    USER_FIELD.SCORE -> {
                        userBO.copy(scoreAccum = params.fied.second as Int)
                    }
                    USER_FIELD.PASSWORD -> {
                        userBO.copy(password = params.fied.second as String)
                    }
                }
                repository.updateUserDetails(newUser)
            }
        } else {
            RESULT_CODES.FAILED
        }
    }
}