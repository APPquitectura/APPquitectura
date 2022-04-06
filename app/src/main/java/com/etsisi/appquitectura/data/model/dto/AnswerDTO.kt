package com.etsisi.appquitectura.data.model.dto

import com.etsisi.appquitectura.domain.model.AnswerBO

class AnswerDTO(
    val title: String? = null,
    val correct: Boolean? = null
) {
    fun toDomain() = AnswerBO(
        title = title.orEmpty(),
        correct = correct ?: false
    )
}