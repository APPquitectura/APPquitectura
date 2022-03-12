package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.helper.FirestoreHelper
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.suspendCancellableCoroutine

class RegisterUseCase(private val auth: FirebaseAuth): UseCase<RegisterUseCase.Params, RegisterUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { WEAK_PASSWORD, EMAIL_ALREADY_EXISTS, EMAIL_MALFORMED, GENERIC_ERROR, DATABASE_ERROR, SUCCESS }

    data class Params(
        val email: String,
        val password: String
    )

    override suspend fun run(params: Params): RESULT_CODES {
        var result = register(params.email, params.password)
        if (result == RESULT_CODES.SUCCESS) {
            result = createUserInDatabase(params.email)
        }
        return result
    }

    private suspend fun register(email: String, password: String): RESULT_CODES =
        suspendCancellableCoroutine { cont ->
            auth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        CurrentUser.instance?.reload()
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
                        cont.resume(RESULT_CODES.SUCCESS, null)
                    },
                    onError = {
                        cont.resume(RESULT_CODES.DATABASE_ERROR, null)
                    }
                )
        }
}