package com.etsisi.appquitectura.data.repository

import androidx.fragment.app.FragmentActivity
import com.etsisi.appquitectura.domain.model.UserBO
import com.etsisi.appquitectura.domain.usecase.CheckUserIsRegisteredUseCase
import com.etsisi.appquitectura.domain.usecase.CheckVerificationCodeUseCase
import com.etsisi.appquitectura.domain.usecase.SignInWithEmailAndPasswordUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.domain.usecase.SignInWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.UpdateUserDetailsUseCase
import com.google.firebase.auth.AuthCredential

interface UsersRepository {
    suspend fun isUserRegistered(email: String): CheckUserIsRegisteredUseCase.RESULT_CODES
    suspend fun register(user: UserBO): RegisterUseCase.RESULT_CODES
    suspend fun createUser(user: UserBO): RegisterUseCase.RESULT_CODES
    suspend fun getUserById(email: String): UserBO
    suspend fun updateUserDetails(user: UserBO): UpdateUserDetailsUseCase.RESULT_CODES
    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInWithEmailAndPasswordUseCase.RESULT_CODES
    suspend fun signInWithCredentials(credential: AuthCredential, context: FragmentActivity): SignInWithCredentialsUseCase.RESULT_CODES
    suspend fun checkVerificationCode(code: String): CheckVerificationCodeUseCase.RESULT_CODES
}