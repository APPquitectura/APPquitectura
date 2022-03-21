package com.etsisi.appquitectura.domain.model

import com.etsisi.appquitectura.data.model.entities.QuestionEntity

data class QuestionBO(
    val id: String?,
    val title: String?
) {
    fun toEntity() = QuestionEntity(
        id = id.orEmpty(),
        title = title.orEmpty()
    )
}

enum class QUESTION_LEVEL(val value: String) {
    D1("D1"),
    D2("D2"),
    D3("D3")
}
