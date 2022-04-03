package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.google.firebase.auth.ActionCodeResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class CheckVerificationCodeUseCase(private val repository: UsersRepository) : UseCase<CheckVerificationCodeUseCase.Params, CheckVerificationCodeUseCase.RESULT_CODES>() {

    private companion object {
        const val VERIFICATION_CODE = "oobCode"
        const val API_KEY = "apiKey"
        const val MODE = "mode"
        const val CONTINUE_URL = "continueUrl"
    }

    enum class RESULT_CODES { SUCESS, GENERIC_ERROR }

    data class Params(
        val dynamicLinkData: PendingDynamicLinkData
    )

    override suspend fun run(params: Params): RESULT_CODES {
       return params.dynamicLinkData?.link?.let { deeplink ->
            val code = deeplink.getQueryParameter(VERIFICATION_CODE).orEmpty()
            return repository.checkVerificationCode(code)
        } ?: RESULT_CODES.GENERIC_ERROR
    }

}