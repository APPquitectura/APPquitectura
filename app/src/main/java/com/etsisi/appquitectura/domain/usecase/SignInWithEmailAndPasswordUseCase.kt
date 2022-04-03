package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.UsersRepository
import java.util.Locale

class SignInWithEmailAndPasswordUseCase(private val repository: UsersRepository) :
    UseCase<SignInWithEmailAndPasswordUseCase.Params, SignInWithEmailAndPasswordUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { EMAIL_INVALID, PASSWORD_INVALID, GENERIC_ERROR, SUCCESS }

    data class Params(
        val email: String,
        val password: String
    )

    override suspend fun run(params: Params): RESULT_CODES {
        return repository.signInWithEmailAndPassword(
            params.email.trim().lowercase(Locale.getDefault()),
            params.password.trim().lowercase(Locale.getDefault())
        )
    }
}