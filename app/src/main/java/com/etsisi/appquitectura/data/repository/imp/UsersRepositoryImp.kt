package com.etsisi.appquitectura.data.repository.imp

import android.database.sqlite.SQLiteException
import com.etsisi.appquitectura.data.datasource.local.UsersLocalDataSource
import com.etsisi.appquitectura.data.datasource.remote.UsersRemoteDataSource
import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.model.QuestionSubject
import com.etsisi.appquitectura.domain.model.UserBO
import com.etsisi.appquitectura.domain.usecase.CheckUserIsRegisteredUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase

class UsersRepositoryImp(
       private val remote: UsersRemoteDataSource,
       private val local: UsersLocalDataSource
): UsersRepository {

    @Throws(SQLiteException::class)
    override suspend fun isUserRegistered(email: String): CheckUserIsRegisteredUseCase.RESULT_CODES {
        return remote.isUserRegistered(email)
    }

    @Throws(SQLiteException::class)
    override suspend fun register(email: String, password: String): RegisterUseCase.RESULT_CODES {
        return remote.register(email, password)
    }

    @Throws(SQLiteException::class)
    override suspend fun createUserInDatabase(name: String?, email: String, subject: QuestionSubject): RegisterUseCase.RESULT_CODES {
        return remote.createUserInFirestore(name, email, subject.value)
    }

    @Throws(SQLiteException::class)
    override suspend fun getUserById(email: String): UserBO {
        return local.getUserById(email)
    }
}