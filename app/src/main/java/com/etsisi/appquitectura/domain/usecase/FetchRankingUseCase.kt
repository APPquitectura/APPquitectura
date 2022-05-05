package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.domain.model.UserBO

class FetchRankingUseCase(private val repository: UsersRepository): UseCase<Unit, List<UserBO>>() {

    override suspend fun run(params: Unit): List<UserBO> {
        return repository.getAllUsers().sortedByDescending { it.rankingPoints }
    }

}