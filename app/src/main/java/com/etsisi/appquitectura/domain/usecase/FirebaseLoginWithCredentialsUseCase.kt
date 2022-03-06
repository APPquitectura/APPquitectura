package com.etsisi.appquitectura.domain.usecase

import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine

class FirebaseLoginWithCredentialsUseCase(private val auth: FirebaseAuth) :
    UseCase<FirebaseLoginWithCredentialsUseCase.Params, FirebaseLoginWithCredentialsUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { SUCESS, GENERIC_ERROR }

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
                                //You are not register in our database
                            }
                            is FirebaseAuthInvalidUserException -> {
                                //Thrown if the credential is malformed or has expired. If credential instanceof EmailAuthCredential it will be thrown if the password is incorrect.
                            }
                            is FirebaseAuthUserCollisionException -> {
                                //thrown if there already exists an account with the email address asserted by the credential. Resolve this case by calling fetchSignInMethodsForEmail(String) and then asking the user to sign in using one of them.
                            }
                            else -> {
                                cont.resume(RESULT_CODES.GENERIC_ERROR, null)
                            }
                        }
                    }
                }
        }
}