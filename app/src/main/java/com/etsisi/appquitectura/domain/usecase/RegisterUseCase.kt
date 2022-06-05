package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.model.enums.UserGender
import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.domain.enums.QuestionSubject
import com.etsisi.appquitectura.domain.model.UserBO

class RegisterUseCase(private val repository: UsersRepository) : UseCase<RegisterUseCase.Params, RegisterUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { WEAK_PASSWORD, EMAIL_ALREADY_EXISTS, EMAIL_MALFORMED, GENERIC_ERROR, DATABASE_ERROR, SUCCESS }

    data class Params(
        val name: String,
        val email: String,
        val password: String,
        val course: QuestionSubject,
        val gender: UserGender,
        val surname: String,
        val city: String,
        val academicRecord: String,
        val academicGroup: String,
    )

    override suspend fun run(params: Params): RESULT_CODES {
        val user = UserBO(
            id = params.email,
            name = params.name,
            email = params.email,
            course = params.course,
            password = params.password,
            gender = params.gender,
            surname = params.surname,
            city = params.city,
            academicRecord = params.academicRecord,
            academicGroup = params.academicGroup,
            gameExperience = 0,
            totalQuestionsAnswered = 0,
            totalCorrectQuestionsAnswered = 0
        )

        return repository.register(user)
    }

}