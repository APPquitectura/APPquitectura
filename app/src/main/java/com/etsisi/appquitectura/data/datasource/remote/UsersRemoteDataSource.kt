package com.etsisi.appquitectura.data.datasource.remote

import com.etsisi.appquitectura.data.helper.FirestoreHelper
import com.etsisi.appquitectura.data.helper.FirestoreNoDocumentExistsException
import com.etsisi.appquitectura.data.model.dto.UserDTO
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.usecase.CheckUserIsRegisteredUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.suspendCancellableCoroutine

class UsersRemoteDataSource(
        private val auth: FirebaseAuth
) {

    suspend fun register(email: String, password: String): RegisterUseCase.RESULT_CODES = suspendCancellableCoroutine { cont ->
        auth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        CurrentUser.instance?.reload()
                        cont.resume(RegisterUseCase.RESULT_CODES.SUCCESS, null)
                    } else {
                        val error = when (result.exception) {
                            is FirebaseAuthWeakPasswordException -> RegisterUseCase.RESULT_CODES.WEAK_PASSWORD
                            is FirebaseAuthInvalidCredentialsException -> RegisterUseCase.RESULT_CODES.EMAIL_MALFORMED
                            is FirebaseAuthUserCollisionException -> RegisterUseCase.RESULT_CODES.EMAIL_ALREADY_EXISTS
                            else -> RegisterUseCase.RESULT_CODES.GENERIC_ERROR
                        }
                        cont.resume(error, null)
                    }
                }
    }

    suspend fun createUserInFirestore(name: String?, email: String, subject: Int): RegisterUseCase.RESULT_CODES =
            suspendCancellableCoroutine { cont ->
                var data = UserDTO(
                        id = CurrentUser.email ?: email,
                        name = CurrentUser.name ?: name.orEmpty(),
                        email = CurrentUser.email ?: email,
                        subject = subject
                )
                FirestoreHelper
                        .writeDocument(
                                collection = Constants.users_collection,
                                document = email,
                                data = data,
                                onSuccess = {
                                    cont.resume(RegisterUseCase.RESULT_CODES.SUCCESS, null)
                                },
                                onError = {
                                    cont.resume(RegisterUseCase.RESULT_CODES.DATABASE_ERROR, null)
                                }
                        )
            }

    suspend fun isUserRegistered(email: String): CheckUserIsRegisteredUseCase.RESULT_CODES = suspendCancellableCoroutine { cont ->
        FirestoreHelper
                .readDocument<UserDTO>(
                        collection = Constants.users_collection,
                        document = email,
                        onSuccess = {
                            cont.resume(CheckUserIsRegisteredUseCase.RESULT_CODES.SUCCESS, null)
                        },
                        onError = {
                            when (it) {
                                is FirestoreNoDocumentExistsException -> {
                                    cont.resume(CheckUserIsRegisteredUseCase.RESULT_CODES.NOT_EXISTS, null)
                                }
                                is FirebaseFirestoreException -> {
                                    cont.resume(CheckUserIsRegisteredUseCase.RESULT_CODES.PERMISIONS_DENIED, null)
                                }
                                else -> {
                                    cont.resume(CheckUserIsRegisteredUseCase.RESULT_CODES.GENERIC_ERROR, null)
                                }
                            }
                        }
                )
    }
}