package com.etsisi.appquitectura.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.suspendCancellableCoroutine

class RegisterUseCase(private val auth: FirebaseAuth): UseCase<RegisterUseCase.Params, RegisterUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { WEAK_PASSWORD, EMAIL_ALREADY_EXISTS, EMAIL_MALFORMED, GENERIC_ERROR, SUCCESS }

    override suspend fun run(params: Params): RESULT_CODES {
        return register(params.email, params.password)
    }

    private suspend fun register(email: String, password: String): RESULT_CODES =
        suspendCancellableCoroutine { cont ->
            auth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        cont.resume(RESULT_CODES.SUCCESS, null)
                    } else {
                        val error = when (result.exception) {
                            is FirebaseAuthWeakPasswordException -> RESULT_CODES.WEAK_PASSWORD
                            is FirebaseAuthInvalidCredentialsException -> RESULT_CODES.EMAIL_MALFORMED
                            is FirebaseAuthUserCollisionException -> RESULT_CODES.EMAIL_ALREADY_EXISTS
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