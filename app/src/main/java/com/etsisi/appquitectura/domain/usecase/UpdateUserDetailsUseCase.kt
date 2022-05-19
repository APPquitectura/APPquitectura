package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.model.UserBO

class UpdateUserDetailsUseCase(
    private val repository: UsersRepository
): UseCase<UpdateUserDetailsUseCase.Params, UserBO?>() {

    enum class RESULT_CODES { SUCCESS, FAILED }

    enum class USER_FIELD { PASSWORD, EXPERIENCE, TOTAL_CORRECT_ANSWERS, TOTAL_ANSWERS }

    data class Params(
        val id: String?,
        val field: Map<USER_FIELD, Any>
        )

    override suspend fun run(params: Params): UserBO? {
        return params.id?.let {
            repository.getUserById(CurrentUser.email.orEmpty())?.let { userBO ->
                var userUpdated = userBO
                params.field.keys.forEach {
                    userUpdated = when(it) {
                        USER_FIELD.PASSWORD -> {
                            userUpdated.copy(password = params.field.get(USER_FIELD.PASSWORD) as String)
                        }
                        USER_FIELD.TOTAL_CORRECT_ANSWERS -> {
                            userUpdated.copy(totalCorrectQuestionsAnswered = (params.field.get(USER_FIELD.TOTAL_CORRECT_ANSWERS) as Int) + userBO.totalCorrectQuestionsAnswered)
                        }
                        USER_FIELD.TOTAL_ANSWERS -> {
                            userUpdated.copy(totalQuestionsAnswered = (params.field.get(USER_FIELD.TOTAL_ANSWERS) as Int) + userBO.totalCorrectQuestionsAnswered)
                        }
                        USER_FIELD.EXPERIENCE -> {
                            userUpdated.copy(gameExperience = (params.field.get(USER_FIELD.EXPERIENCE) as Long) + userBO.gameExperience)
                        }
                    }
                }
                userUpdated.takeIf { repository.updateUserDetails(userUpdated) == RESULT_CODES.SUCCESS }
            }
        }
    }
}