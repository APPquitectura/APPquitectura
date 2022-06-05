package com.etsisi.appquitectura.domain.model

import com.etsisi.appquitectura.data.model.dto.UserDTO
import com.etsisi.appquitectura.data.model.entities.UserEntity
import com.etsisi.appquitectura.data.model.enums.UserGender
import com.etsisi.appquitectura.domain.enums.QuestionSubject
import com.etsisi.appquitectura.presentation.utils.EMPTY

data class UserBO(
    val id: String,
    val name: String,
    val email: String,
    val password: String = String.EMPTY,
    val course: QuestionSubject,
    val gender: UserGender,
    val surname: String,
    val city: String,
    val academicRecord: String,
    val academicGroup: String,
    val gameExperience: Long,
    val totalQuestionsAnswered: Int,
    val totalCorrectQuestionsAnswered: Int
) {
    val totalIncorrectQuestionsAnswered
        get() = totalQuestionsAnswered - totalCorrectQuestionsAnswered

    fun toDTO() = UserDTO(
        email = email,
        id= id,
        name = name,
        subject = course.value,
        gender = gender.value,
        surname = surname,
        city = city,
        academicRecord = academicRecord,
        academicGroup = academicGroup,
        gameExperience = gameExperience,
        totalQuestionsAnswered = totalQuestionsAnswered,
        totalCorrectQuestionsAnswered = totalCorrectQuestionsAnswered
    )

    fun toEntity() = UserEntity(
        id = email,
        name = name,
        email = email,
        subject = course.value,
        gender = gender.value,
        surname = surname,
        city = city,
        academicGroup = academicGroup,
        academicRecord = academicRecord,
        gameExperience = gameExperience,
        totalQuestionsAnswered = totalQuestionsAnswered,
        totalCorrectQuestionsAnswered = totalCorrectQuestionsAnswered
    )
}
