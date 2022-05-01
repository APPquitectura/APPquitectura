package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.domain.model.CurrentUser

class UpdateUserDetailsUseCase(
    private val repository: UsersRepository
): UseCase<UpdateUserDetailsUseCase.Params, UpdateUserDetailsUseCase.RESULT_CODES>() {

    enum class RESULT_CODES { SUCCESS, FAILED }

    enum class USER_FIELD { PASSWORD, SCORE_ACCUM, TOTAL_CORRECT_ANSWERS, TOTAL_ANSWERS }

    data class Params(val field: Map<USER_FIELD, Any>)

    override suspend fun run(params: Params): RESULT_CODES {
        return if (!CurrentUser.email.isNullOrBlank()) {
            repository.getUserById(CurrentUser.email.orEmpty())?.let { userBO ->
                var userUpdated = userBO
                params.field.keys.onEach {
                    userUpdated = when(it) {
                        USER_FIELD.SCORE_ACCUM -> {
                            userUpdated.copy(gameExperience = (params.field.get(USER_FIELD.SCORE_ACCUM) as Int) + userBO.gameExperience)
                        }
                        USER_FIELD.PASSWORD -> {
                            userUpdated.copy(password = params.field.get(USER_FIELD.PASSWORD) as String)
                        }
                        USER_FIELD.TOTAL_CORRECT_ANSWERS -> {
                            userUpdated.copy(totalCorrectQuestionsAnswered = (params.field.get(USER_FIELD.TOTAL_CORRECT_ANSWERS) as Int) + userBO.totalCorrectQuestionsAnswered)
                        }
                        USER_FIELD.TOTAL_ANSWERS -> {
                            userUpdated.copy(totalQuestionsAnswered = (params.field.get(USER_FIELD.TOTAL_ANSWERS) as Int) + userBO.totalCorrectQuestionsAnswered)
                        }
                    }
                }
                repository.updateUserDetails(userUpdated)
            }
        } else {
            RESULT_CODES.FAILED
        }
    }
}