package com.etsisi.appquitectura.domain.usecase

import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine

class GoogleLoginUseCase(private val auth: FirebaseAuth) : UseCase<GoogleLoginUseCase.Params, GoogleLoginUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { SUCESS, GENERIC_ERROR}

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
                        cont.resume(RESULT_CODES.GENERIC_ERROR, null)
                    }
                }
        }
}