package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.domain.model.CurrentUser
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class LogOutUseCase: UseCase<LogOutUseCase.Params, Unit>() {

    data class Params(val googleSignInClient: GoogleSignInClient)

    override suspend fun run(params: Params) {
        CurrentUser.signOut()
        params.googleSignInClient.revokeAccess()
    }
}