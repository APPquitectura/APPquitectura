package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.domain.model.CurrentUser
import com.google.firebase.auth.ActionCodeResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class CheckVerificationCodeUseCase(private val auth: FirebaseAuth) : UseCase<CheckVerificationCodeUseCase.Params, CheckVerificationCodeUseCase.RESULT_CODES>() {

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
            return verifyCode(code)
        } ?: RESULT_CODES.GENERIC_ERROR
    }

    @ExperimentalCoroutinesApi
    private suspend fun verifyCode(code: String): RESULT_CODES =
        suspendCancellableCoroutine { cont ->
            code.ifBlank {
                cont.resume(RESULT_CODES.GENERIC_ERROR, null)
            }
            auth
                .checkActionCode(code)
                .addOnSuccessListener { action ->
                    if (action.operation == ActionCodeResult.VERIFY_EMAIL) {
                        auth
                            .applyActionCode(code)
                            .addOnSuccessListener {
                                CurrentUser.instance?.reload()
                                cont.resume(RESULT_CODES.SUCESS, null)
                            }
                            .addOnFailureListener {
                                cont.resume(RESULT_CODES.GENERIC_ERROR, null)
                            }
                    }
                }
                .addOnFailureListener {
                    cont.resume(RESULT_CODES.GENERIC_ERROR, null)
                }
        }
}