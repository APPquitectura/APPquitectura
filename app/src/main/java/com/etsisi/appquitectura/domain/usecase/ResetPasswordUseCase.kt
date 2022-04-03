package com.etsisi.appquitectura.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine

class ResetPasswordUseCase(private val auth: FirebaseAuth): UseCase<ResetPasswordUseCase.Params, ResetPasswordUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { SUCCESS, ERROR }

    data class Params(
            val email: String
    )

    override suspend fun run(params: Params): RESULT_CODES  = suspendCancellableCoroutine { cont ->
        auth
                .sendPasswordResetEmail(params.email)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        cont.resume(RESULT_CODES.SUCCESS, null)
                    } else {
                        cont.resume(RESULT_CODES.ERROR, null)
                    }
                }
    }
}