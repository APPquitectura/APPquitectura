package com.etsisi.appquitectura.data.model.dto

import com.etsisi.appquitectura.domain.enums.QuestionLevel
import com.etsisi.appquitectura.domain.enums.QuestionSubject
import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.model.AnswerBO
import com.etsisi.appquitectura.domain.model.QuestionBO

data class QuestionDTO(
    val id: String? = null,
    val pregunta_texto: String? = null,
    val pregunta_imagen: String? = null,
    val dificultad: String? = null,
    val asignatura: String? = null,
    val respuestas: List<String>? = null,
    val etiquetas: List<String>? = null
) {
    private companion object {
        const val CORRECT_ANSWER_INDEX = 0
    }

    fun toDomain() =
        QuestionBO(
            id = id,
            title = pregunta_texto,
            subject = QuestionSubject.parseSubject(asignatura),
            level = QuestionLevel.parseLevel(dificultad),
            labels = etiquetas?.map { QuestionTopic.parseTopic(it.trim()) }.orEmpty(),
            imageRef = pregunta_imagen.orEmpty(),
            answers = respuestas?.mapIndexed { index, s ->
                AnswerBO(title = s, correct = index == CORRECT_ANSWER_INDEX)
            }.orEmpty()
        )
}
