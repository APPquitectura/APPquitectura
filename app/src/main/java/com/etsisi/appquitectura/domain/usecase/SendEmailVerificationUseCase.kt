package com.etsisi.appquitectura.domain.usecase

class SendEmailVerificationUseCase: UseCase<SendEmailVerificationUseCase.Params, Unit>() {

    override suspend fun run(params: Params) {
        TODO("Not yet implemented")
    }

    data class Params(
        val packageName: String,
        val installIfNotAvailable: Boolean = true,
        val handleCodeInApp: Boolean = true
    )
}