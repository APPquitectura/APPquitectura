package com.etsisi.appquitectura.domain.usecase

import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine

class FirebaseLoginWithCredentialsUseCase(private val auth: FirebaseAuth) :
    UseCase<FirebaseLoginWithCredentialsUseCase.Params, FirebaseLoginWithCredentialsUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { SUCESS, INVALID_USER, CREDENTIALS_MALFORMED, COLLISION }

    data class Params(val token: String, val context: FragmentActivity)

    override suspend fun run(params: Params): RESULT_CODES {
        val credentials = GoogleAuthProvider.getCredential(params.token, null)
        return login(credentials, params.context)
    }

    private suspend fun login(credential: AuthCredential, context: FragmentActivity): RESULT_CODES =
        suspendCancellableCoroutine { cont ->
            auth
                .signInWithCredential(credential)
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        cont.resume(RESULT_CODES.SUCESS, null)
                    } else {
                        when (task.exception) {
                            is FirebaseAuthInvalidUserException -> {
                                cont.resume(RESULT_CODES.INVALID_USER, null)
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                cont.resume(RESULT_CODES.CREDENTIALS_MALFORMED, null)
                            }
                            is FirebaseAuthUserCollisionException -> {
                                cont.resume(RESULT_CODES.COLLISION, null)
                            }
                        }
                    }
                }
        }
}