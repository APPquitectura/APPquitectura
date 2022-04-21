package com.etsisi.appquitectura.data.repository.imp

import androidx.fragment.app.FragmentActivity
import com.etsisi.appquitectura.data.datasource.local.UsersLocalDataSource
import com.etsisi.appquitectura.data.datasource.remote.UsersRemoteDataSource
import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.model.UserBO
import com.etsisi.appquitectura.domain.usecase.CheckUserIsRegisteredUseCase
import com.etsisi.appquitectura.domain.usecase.CheckVerificationCodeUseCase
import com.etsisi.appquitectura.domain.usecase.SignInWithEmailAndPasswordUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.domain.usecase.SignInWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.UpdateUserDetailsUseCase
import com.google.firebase.auth.AuthCredential

class UsersRepositoryImp(
       private val remote: UsersRemoteDataSource,
       private val local: UsersLocalDataSource
): UsersRepository {

    override suspend fun isUserRegistered(email: String): CheckUserIsRegisteredUseCase.RESULT_CODES {
        return remote.isUserRegistered(email)
    }

    override suspend fun register(user: UserBO): RegisterUseCase.RESULT_CODES {
        val result = remote.register(user.email, user.password)
        if (result == RegisterUseCase.RESULT_CODES.SUCCESS) {
            createUser(user)
        }
        return result
    }

    override suspend fun createUser(userBO: UserBO): RegisterUseCase.RESULT_CODES {
        local.addUsers(userBO)
        return remote.createUserInFirestore(userBO)
    }

    override suspend fun getUserById(email: String): UserBO {
        return local.getUserById(email) ?: remote.getUserById(email).also {
            local.addUsers(it)
        }
    }

    override suspend fun updateUserDetails(user: UserBO): UpdateUserDetailsUseCase.RESULT_CODES {
        return remote?.updateUserDetails(user.toDTO()).also {
            local.updateUserDetails(user.toEntity())
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): SignInWithEmailAndPasswordUseCase.RESULT_CODES {
        val result = remote.signInWithEmailAndPassword(email, password)
        if (result == SignInWithEmailAndPasswordUseCase.RESULT_CODES.SUCCESS) {
            getUserById(email)
        }
        return result
    }

    override suspend fun signInWithCredentials(
        credential: AuthCredential,
        context: FragmentActivity
    ): SignInWithCredentialsUseCase.RESULT_CODES {
        val result = remote.signInWithCredentials(credential, context)
        if (result == SignInWithCredentialsUseCase.RESULT_CODES.SUCESS) {
            CurrentUser.instance?.let {
                it.reload()
                it.email?.let { getUserById(it) }
            }
        }
        return result
    }

    override suspend fun checkVerificationCode(code: String): CheckVerificationCodeUseCase.RESULT_CODES {
        return remote.checkVerificationCode(code)
    }
}