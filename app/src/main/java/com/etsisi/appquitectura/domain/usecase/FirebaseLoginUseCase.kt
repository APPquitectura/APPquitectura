package com.etsisi.appquitectura.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.suspendCancellableCoroutine

class FirebaseLoginUseCase(private val auth: FirebaseAuth): UseCase<FirebaseLoginUseCase.Params, FirebaseLoginUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { EMAIL_INVALID, PASSWORD_INVALID, GENERIC_ERROR, SUCCESS }

    override suspend fun run(params: Params): RESULT_CODES {
        return login(params.email, params.password)
    }

    private suspend fun login(email: String, password: String): RESULT_CODES =
        suspendCancellableCoroutine { cont ->
            auth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        cont.resume(RESULT_CODES.SUCCESS, null)
                    } else {
                        val error = when (result.exception) {
                            is FirebaseAuthInvalidCredentialsException -> RESULT_CODES.PASSWORD_INVALID
                            is FirebaseAuthInvalidUserException -> RESULT_CODES.EMAIL_INVALID
                            else -> RESULT_CODES.GENERIC_ERROR
                        }
                        cont.resume(error, null)
                    }
                }
        }

    data class Params(
        val email: String,
        val password: String
    )
}