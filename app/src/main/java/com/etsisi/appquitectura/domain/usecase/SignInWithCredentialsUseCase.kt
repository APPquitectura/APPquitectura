package com.etsisi.appquitectura.domain.usecase

import androidx.fragment.app.FragmentActivity
import com.etsisi.appquitectura.data.repository.UsersRepository
import com.google.firebase.auth.GoogleAuthProvider

class SignInWithCredentialsUseCase(private val repository: UsersRepository) :
    UseCase<SignInWithCredentialsUseCase.Params, SignInWithCredentialsUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { SUCESS, INVALID_USER, CREDENTIALS_MALFORMED, DATABASE_ERROR, COLLISION }

    data class Params(
        val token: String,
        val context: FragmentActivity,
        val email: String
    )

    override suspend fun run(params: Params): RESULT_CODES {
        val credentials = GoogleAuthProvider.getCredential(params.token, null)
        return repository.signInWithCredentials(credentials, params.context)
    }

}