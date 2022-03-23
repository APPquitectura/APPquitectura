package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.helper.FirestoreHelper
import com.etsisi.appquitectura.domain.model.UserBO
import com.etsisi.appquitectura.utils.Constants
import kotlinx.coroutines.suspendCancellableCoroutine

class CheckUserIsRegisteredUseCase: UseCase<CheckUserIsRegisteredUseCase.Params, Boolean>() {

    data class Params(
        val userId: String
    )

    override suspend fun run(params: Params): Boolean {
        return userExistsInDatabase(params.userId)
    }

    private suspend fun userExistsInDatabase(userId: String): Boolean = suspendCancellableCoroutine { cont ->
        FirestoreHelper
            .readDocument<UserBO>(
                collection = Constants.questions_collection,
                document = userId,
                onSuccess = {cont.resume(true, null)},
                onError = {cont.resume(false, null)}
            )
    }
}