package com.etsisi.appquitectura.data.repository.imp

import android.database.sqlite.SQLiteException
import androidx.fragment.app.FragmentActivity
import com.etsisi.appquitectura.data.datasource.local.UsersLocalDataSource
import com.etsisi.appquitectura.data.datasource.remote.UsersRemoteDataSource
import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.domain.model.UserBO
import com.etsisi.appquitectura.domain.usecase.CheckUserIsRegisteredUseCase
import com.etsisi.appquitectura.domain.usecase.SignInWithEmailAndPasswordUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.domain.usecase.SignInWithCredentialsUseCase
import com.google.firebase.auth.AuthCredential

class UsersRepositoryImp(
       private val remote: UsersRemoteDataSource,
       private val local: UsersLocalDataSource
): UsersRepository {

    @Throws(SQLiteException::class)
    override suspend fun isUserRegistered(email: String): CheckUserIsRegisteredUseCase.RESULT_CODES {
        return remote.isUserRegistered(email)
    }

    @Throws(SQLiteException::class)
    override suspend fun register(user: UserBO): RegisterUseCase.RESULT_CODES {
        val result = remote.register(user.email, user.password)
        if (result == RegisterUseCase.RESULT_CODES.SUCCESS) {
            createUser(user)
        }
        return result
    }

    @Throws(SQLiteException::class)
    override suspend fun createUser(userBO: UserBO): RegisterUseCase.RESULT_CODES {
        local.addUsers(userBO)
        return remote.createUserInFirestore(userBO)
    }

    @Throws(SQLiteException::class)
    override suspend fun getUserById(email: String): UserBO {
        return local.getUserById(email) ?: remote.getUserById(email).also {
            local.addUsers(it)
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
        return remote.signInWithCredentials(credential, context)
    }
}