package com.etsisi.appquitectura.domain.model

import com.etsisi.appquitectura.data.model.dto.UserDTO
import com.etsisi.appquitectura.data.model.entities.UserEntity
import com.etsisi.appquitectura.presentation.utils.EMPTY

data class UserBO(
    val id: String,
    val name: String,
    val email: String,
    val password: String = String.EMPTY,
    val subject: QuestionSubject,
    val gameExperience: Long,
    val rankingPoints: Long,
    val totalQuestionsAnswered: Int,
    val totalCorrectQuestionsAnswered: Int
) {
    val totalIncorrectQuestionsAnswered
        get() = totalQuestionsAnswered - totalCorrectQuestionsAnswered

    fun toDTO() = UserDTO(
        email = email,
        id= id,
        name = name,
        subject = subject.value,
        gameExperience = gameExperience,
        rankingPoints = rankingPoints,
        totalQuestionsAnswered = totalQuestionsAnswered,
        totalCorrectQuestionsAnswered = totalCorrectQuestionsAnswered
    )

    fun toEntity() = UserEntity(
        id = email,
        name = name,
        email = email,
        subject = subject.value,
        gameExperience = gameExperience,
        rankingPoints = rankingPoints,
        totalQuestionsAnswered = totalQuestionsAnswered,
        totalCorrectQuestionsAnswered = totalCorrectQuestionsAnswered
    )
}
