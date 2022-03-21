package com.etsisi.appquitectura.data.model.dto

import com.etsisi.appquitectura.domain.model.QuestionBO

data class QuestionDTO(
    val id: String? = null,
    val title: String? = null
) {
    fun toDomain() =
        QuestionBO(
            id = id,
            title = title
        )

}
