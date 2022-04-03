package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.helper.FirestoreHelper
import com.etsisi.appquitectura.data.helper.FirestoreNoDocumentExistsException
import com.etsisi.appquitectura.data.model.dto.UserDTO
import com.etsisi.appquitectura.utils.Constants
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.suspendCancellableCoroutine

class CheckUserIsRegisteredUseCase: UseCase<CheckUserIsRegisteredUseCase.Params, Boolean>() {

    data class Params(
        val email: String
    )

    enum class RESULT_CODES { NOT_EXISTS, GENERIC_ERROR, PERMISIONS_DENIED, SUCCESS }

    override suspend fun run(params: Params): Boolean {
        return userExistsInDatabase(params.email) == RESULT_CODES.SUCCESS
    }

    private suspend fun userExistsInDatabase(email: String): RESULT_CODES = suspendCancellableCoroutine { cont ->
        FirestoreHelper
            .readDocument<UserDTO>(
                collection = Constants.users_collection,
                document = email,
                onSuccess = {cont.resume(RESULT_CODES.SUCCESS, null)},
                onError = {
                    when(it) {
                        is FirestoreNoDocumentExistsException -> {
                            cont.resume(RESULT_CODES.NOT_EXISTS, null)
                        }
                        is FirebaseFirestoreException -> {
                            cont.resume(RESULT_CODES.PERMISIONS_DENIED, null)
                        }
                        else -> {
                            cont.resume(RESULT_CODES.GENERIC_ERROR, null)
                        }
                    }
                }
            )
    }
}