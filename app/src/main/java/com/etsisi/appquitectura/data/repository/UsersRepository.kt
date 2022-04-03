package com.etsisi.appquitectura.data.repository

import com.etsisi.appquitectura.domain.model.QuestionSubject
import com.etsisi.appquitectura.domain.model.UserBO
import com.etsisi.appquitectura.domain.usecase.CheckUserIsRegisteredUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase

interface UsersRepository {
    suspend fun isUserRegistered(email: String): CheckUserIsRegisteredUseCase.RESULT_CODES
    suspend fun register(email: String, password: String): RegisterUseCase.RESULT_CODES
    suspend fun createUserInDatabase(name: String?, email: String, subject: QuestionSubject): RegisterUseCase.RESULT_CODES
    suspend fun getUserById(email: String): UserBO
}