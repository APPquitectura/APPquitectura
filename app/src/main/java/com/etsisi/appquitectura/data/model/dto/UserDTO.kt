package com.etsisi.appquitectura.data.model.dto

import com.etsisi.appquitectura.domain.model.QuestionSubject
import com.etsisi.appquitectura.domain.model.UserBO
import com.etsisi.appquitectura.presentation.utils.EMPTY

data class UserDTO (
        val email: String = String.EMPTY,
        val id: String = String.EMPTY,
        val name: String = String.EMPTY,
        val subject: String = QuestionSubject.UNKNOWN.value,
        val gameExperience: Long = 0,
        val totalQuestionsAnswered: Int = 0,
        val totalCorrectQuestionsAnswered: Int = 0
): FirestoreDTO() {
        fun toDomain() = UserBO(
                email = email,
                id = id,
                name = name,
                subject = QuestionSubject.parseSubject(subject),
                gameExperience = gameExperience,
                totalQuestionsAnswered = totalQuestionsAnswered,
                totalCorrectQuestionsAnswered = totalCorrectQuestionsAnswered
        )
}