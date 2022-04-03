package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.domain.model.QuestionSubject
import com.etsisi.appquitectura.domain.model.UserBO

class RegisterUseCase(private val repository: UsersRepository) : UseCase<RegisterUseCase.Params, RegisterUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { WEAK_PASSWORD, EMAIL_ALREADY_EXISTS, EMAIL_MALFORMED, GENERIC_ERROR, DATABASE_ERROR, SUCCESS }

    data class Params(
            val name: String? = null,
            val email: String,
            val password: String,
            val subject: QuestionSubject
    )

    override suspend fun run(params: Params): RESULT_CODES {
        val user = UserBO(
            id = params.email,
            name = params.name.orEmpty(),
            email = params.email,
            subject = params.subject,
            password = params.password
        )

        return repository.register(user)
    }

}