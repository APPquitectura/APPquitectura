package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.BuildConfig
import com.etsisi.appquitectura.utils.Constants
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine

class ResetPasswordUseCase(private val auth: FirebaseAuth): UseCase<ResetPasswordUseCase.Params, ResetPasswordUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { SUCCESS, ERROR }

    data class Params(
            val email: String
    )

    override suspend fun run(params: Params): RESULT_CODES  = suspendCancellableCoroutine { cont ->
        val acs = ActionCodeSettings
                .newBuilder()
                .setUrl(Constants.resetPasswordDeepLink)
                .setDynamicLinkDomain(Constants.DYNAMIC_LINK_PREFIX)
                .setAndroidPackageName(BuildConfig.APPLICATION_ID, true, BuildConfig.VERSION_CODE.toString())
                .build()

        auth
                .sendPasswordResetEmail(params.email, acs)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        cont.resume(RESULT_CODES.SUCCESS, null)
                    } else {
                        cont.resume(RESULT_CODES.ERROR, null)
                    }
                }
    }
}