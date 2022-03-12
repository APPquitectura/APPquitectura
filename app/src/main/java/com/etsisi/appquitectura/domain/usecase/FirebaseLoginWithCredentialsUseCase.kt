package com.etsisi.appquitectura.domain.usecase

import androidx.fragment.app.FragmentActivity
import com.etsisi.appquitectura.data.helper.FirestoreHelper
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.utils.Constants
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine

class FirebaseLoginWithCredentialsUseCase(private val auth: FirebaseAuth) :
    UseCase<FirebaseLoginWithCredentialsUseCase.Params, FirebaseLoginWithCredentialsUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { SUCESS, INVALID_USER, CREDENTIALS_MALFORMED, DATABASE_ERROR, COLLISION }

    data class Params(
        val token: String,
        val context: FragmentActivity,
        val createUser: Boolean,
        val email: String
    )

    override suspend fun run(params: Params): RESULT_CODES {
        val credentials = GoogleAuthProvider.getCredential(params.token, null)
        var result = login(credentials, params.context)
        if (result == RESULT_CODES.SUCESS && params.createUser) {
            result = createUserInDatabase(CurrentUser?.email ?: params.email)
        }
        return result
    }

    private suspend fun login(credential: AuthCredential, context: FragmentActivity): RESULT_CODES =
        suspendCancellableCoroutine { cont ->
            auth
                .signInWithCredential(credential)
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        CurrentUser.instance?.reload()
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

    private suspend fun createUserInDatabase(email: String): RESULT_CODES =
        suspendCancellableCoroutine { cont ->
            var data = CurrentUser.toDomain()
            if (data.id.isBlank()) {
                data = data.copy(id = email)
            }
            FirestoreHelper
                .writeDocument(
                    collection = Constants.users_collection,
                    document = data.id,
                    data = data,
                    onSuccess = {
                        cont.resume(RESULT_CODES.SUCESS, null)
                    },
                    onError = {
                        cont.resume(RESULT_CODES.DATABASE_ERROR, null)
                    }
                )
        }

}