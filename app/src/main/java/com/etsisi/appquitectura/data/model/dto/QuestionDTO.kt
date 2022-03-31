package com.etsisi.appquitectura.data.model.dto

import com.etsisi.appquitectura.domain.model.QuestionAge
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.domain.model.QuestionLevel
import com.etsisi.appquitectura.domain.model.QuestionTopic

data class QuestionDTO(
    val id: String? = null,
    val title: String? = null,
    val level: Int? = null,
    val age: Int? = null,
    val topic: Int? = null
) {
    fun toDomain() =
        QuestionBO(
            id = id,
            title = title,
            level = QuestionLevel.parseLevel(level),
            age = QuestionAge.parseAge(age),
            topic = QuestionTopic.parseTopic(topic)
        )
}
