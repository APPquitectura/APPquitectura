package com.etsisi.appquitectura.domain.usecase

import android.app.Application
import com.etsisi.appquitectura.BuildConfig
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.utils.Constants
import com.google.firebase.auth.ActionCodeSettings
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.java.KoinJavaComponent

class SendEmailVerificationUseCase: UseCase<SendEmailVerificationUseCase.Params, SendEmailVerificationUseCase.RESULT_CODES>() {
    enum class RESULT_CODES { SUCESS, GENERIC_ERROR }

    override suspend fun run(params: Params): RESULT_CODES {
        return sendEmail(params)
    }

    private suspend fun sendEmail(params: Params): RESULT_CODES = suspendCancellableCoroutine { cont ->
        val acs = ActionCodeSettings
            .newBuilder()
            .setUrl(params.urlDeepLink)
            .setDynamicLinkDomain(params.dynamicLinkPrefix)
            .setHandleCodeInApp(params.handleCodeInApp)
            .setAndroidPackageName(params.packageName, true, BuildConfig.VERSION_CODE.toString())
            .build()

        CurrentUser
            .instance
            ?.sendEmailVerification(acs)
            ?.addOnSuccessListener {
                cont.resume(RESULT_CODES.SUCESS, null)
            }
            ?.addOnFailureListener {
                cont.resume(RESULT_CODES.GENERIC_ERROR, null)
            }
    }

    data class Params(
        val packageName: String = KoinJavaComponent.getKoin().get<Application>().packageName,
        val installIfNotAvailable: Boolean = true,
        val handleCodeInApp: Boolean = true,
        val urlDeepLink: String = Constants.sendEmailVerificationDeeplink,
        val dynamicLinkPrefix: String = Constants.dynamicLinkPrefix
    )
}