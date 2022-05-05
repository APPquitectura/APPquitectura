package com.etsisi.appquitectura.data.datasource.remote

import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.etsisi.appquitectura.data.helper.FirestoreHelper
import com.etsisi.appquitectura.data.helper.FirestoreNoDocumentExistsException
import com.etsisi.appquitectura.data.model.dto.UserDTO
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.usecase.CheckUserIsRegisteredUseCase
import com.etsisi.appquitectura.domain.usecase.CheckVerificationCodeUseCase
import com.etsisi.appquitectura.domain.usecase.SignInWithEmailAndPasswordUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.domain.usecase.SignInWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.UpdateUserDetailsUseCase
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.presentation.utils.getMethodName
import com.etsisi.appquitectura.utils.Constants
import com.google.firebase.auth.ActionCodeResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.suspendCancellableCoroutine

class UsersRemoteDataSource(
    private val auth: FirebaseAuth
) {

    suspend fun register(email: String, password: String): RegisterUseCase.RESULT_CODES =
        suspendCancellableCoroutine { cont ->
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

    suspend fun createUserInFirestore(userDTO: UserDTO): RegisterUseCase.RESULT_CODES =
        suspendCancellableCoroutine { cont ->
            var data = userDTO
            FirestoreHelper
                .writeDocument(
                    collection = Constants.users_collection,
                    document = userDTO.email,
                    data = data,
                    onSuccess = {
                        cont.resume(RegisterUseCase.RESULT_CODES.SUCCESS, null)
                    },
                    onError = {
                        cont.resume(RegisterUseCase.RESULT_CODES.DATABASE_ERROR, null)
                    }
                )
        }

    suspend fun isUserRegistered(email: String): CheckUserIsRegisteredUseCase.RESULT_CODES =
        suspendCancellableCoroutine { cont ->
            FirestoreHelper
                .readDocument<UserDTO>(
                    collection = Constants.users_collection,
                    document = email,
                    onSuccess = {
                        cont.resume(CheckUserIsRegisteredUseCase.RESULT_CODES.SUCCESS, null)
                    },
                    onError = {
                        Log.e(TAG, "${getMethodName(object {}.javaClass)} ${it?.message}")
                        when (it) {
                            is FirestoreNoDocumentExistsException -> {
                                cont.resume(
                                    CheckUserIsRegisteredUseCase.RESULT_CODES.NOT_EXISTS,
                                    null
                                )
                            }
                            is FirebaseFirestoreException -> {
                                cont.resume(
                                    CheckUserIsRegisteredUseCase.RESULT_CODES.PERMISIONS_DENIED,
                                    null
                                )
                            }
                            else -> {
                                cont.resume(
                                    CheckUserIsRegisteredUseCase.RESULT_CODES.GENERIC_ERROR,
                                    null
                                )
                            }
                        }
                    }
                )
        }

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): SignInWithEmailAndPasswordUseCase.RESULT_CODES = suspendCancellableCoroutine { cont ->
        auth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    cont.resume(SignInWithEmailAndPasswordUseCase.RESULT_CODES.SUCCESS, null)
                } else {
                    Log.e(TAG, "${getMethodName(object {}.javaClass)} ${result.exception?.message}")
                    val error = when (result.exception) {
                        is FirebaseAuthInvalidCredentialsException -> SignInWithEmailAndPasswordUseCase.RESULT_CODES.PASSWORD_INVALID
                        is FirebaseAuthInvalidUserException -> SignInWithEmailAndPasswordUseCase.RESULT_CODES.EMAIL_INVALID
                        else -> SignInWithEmailAndPasswordUseCase.RESULT_CODES.GENERIC_ERROR
                    }
                    cont.resume(error, null)
                }
            }
    }

    suspend fun getUserById(email: String): UserDTO = suspendCancellableCoroutine { cont ->
        FirestoreHelper
            .readDocument<UserDTO>(
                collection = Constants.users_collection,
                document = email,
                onSuccess = {
                    cont.resume(it, null)
                },
                onError = {
                    Log.e(TAG, "${getMethodName(object {}.javaClass)} ${it.message}")
                }
            )
    }

    suspend fun signInWithCredentials(
        credential: AuthCredential,
        context: FragmentActivity
    ): SignInWithCredentialsUseCase.RESULT_CODES =
        suspendCancellableCoroutine { cont ->
            auth
                .signInWithCredential(credential)
                .addOnCompleteListener(context) { task ->
                    if (task.isSuccessful) {
                        CurrentUser.instance?.reload()
                        cont.resume(SignInWithCredentialsUseCase.RESULT_CODES.SUCESS, null)
                    } else {
                        Log.e(
                            TAG,
                            "${getMethodName(object {}.javaClass)} ${task.exception?.message}"
                        )
                        when (task.exception) {
                            is FirebaseAuthInvalidUserException -> {
                                cont.resume(
                                    SignInWithCredentialsUseCase.RESULT_CODES.INVALID_USER,
                                    null
                                )
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                cont.resume(
                                    SignInWithCredentialsUseCase.RESULT_CODES.CREDENTIALS_MALFORMED,
                                    null
                                )
                            }
                            is FirebaseAuthUserCollisionException -> {
                                cont.resume(
                                    SignInWithCredentialsUseCase.RESULT_CODES.COLLISION,
                                    null
                                )
                            }
                        }
                    }
                }
        }

    suspend fun checkVerificationCode(code: String): CheckVerificationCodeUseCase.RESULT_CODES =
        suspendCancellableCoroutine { cont ->
            code.ifBlank {
                cont.resume(CheckVerificationCodeUseCase.RESULT_CODES.GENERIC_ERROR, null)
            }
            auth
                .checkActionCode(code)
                .addOnSuccessListener { action ->
                    if (action.operation == ActionCodeResult.VERIFY_EMAIL) {
                        auth
                            .applyActionCode(code)
                            .addOnSuccessListener {
                                CurrentUser.instance?.reload()
                                cont.resume(CheckVerificationCodeUseCase.RESULT_CODES.SUCESS, null)
                            }
                            .addOnFailureListener {
                                cont.resume(
                                    CheckVerificationCodeUseCase.RESULT_CODES.GENERIC_ERROR,
                                    null
                                )
                            }
                    }
                }
                .addOnFailureListener {
                    cont.resume(CheckVerificationCodeUseCase.RESULT_CODES.GENERIC_ERROR, null)
                }
        }

    suspend fun updateUserDetails(userDTO: UserDTO): UpdateUserDetailsUseCase.RESULT_CODES =
        suspendCancellableCoroutine { cont ->
            FirestoreHelper.writeDocument(
                collection = Constants.users_collection,
                document = userDTO.id,
                data = userDTO,
                onSuccess = {
                    cont.resume(UpdateUserDetailsUseCase.RESULT_CODES.SUCCESS, null)
                },
                onError = {
                    cont.resume(UpdateUserDetailsUseCase.RESULT_CODES.FAILED, null)
                }
            )
        }

    suspend fun getAllUsers(): List<UserDTO> = suspendCancellableCoroutine { cont ->
        FirestoreHelper.readDocumentsOfACollection<UserDTO>(
            collection = Constants.users_collection,
            onSuccess = {
                cont.resume(it, null)
            },
            onError = {
                cont.resume(emptyList(), null)
            }
        )
    }
}