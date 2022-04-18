package com.etsisi.appquitectura.domain.model

import android.os.Parcelable
import com.etsisi.appquitectura.data.helper.FireStorageHelper
import com.etsisi.appquitectura.data.model.entities.QuestionEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionBO(
    val id: String?,
    val title: String?,
    val subject: QuestionSubject,
    val level: QuestionLevel,
    val labels: List<String>,
    val imageRef: String,
    val answers: List<AnswerBO>
): Parcelable {

    fun getImageFirestorageReference() = FireStorageHelper.getImageReference(imageRef)

    fun toEntity() = QuestionEntity(
        id = id.orEmpty(),
        title = title.orEmpty(),
        subject = subject.value,
        level = level.value,
        labels = labels.joinToString(),
        imageRef = imageRef,
        answers = answers
    )
}

enum class QuestionSubject(val value: String) {
    COMPOSICION("COMPOSICIÓN"),
    INTRODUCCION("INTRODUCCIÓN"),
    UNKNOWN("DESCONOCIDO");

    companion object {
        fun parseSubject(subject: String?) = values().find { it.value.equals(subject, true) } ?: UNKNOWN
    }
}

enum class QuestionLevel(val value: String) {
    UNKNOWN("D0"),
    EASY("D1"),
    NORMAL("D2"),
    DIFFICULT("D3");

    companion object {
        fun parseLevel(x: String?): QuestionLevel {
            return when (x) {
                EASY.value -> EASY
                NORMAL.value -> NORMAL
                DIFFICULT.value -> DIFFICULT
                else -> UNKNOWN
            }
        }
    }
}

enum class QuestionAge(val value: String) {
    UNKNOWN("DESCONOCIDO"),
    OLD("ANTIGÜEDAD"),
    MIDDLE_AGE("EDAD MEDIA"),
    MODERN_AGE("EDAD MODERNA"),
    CONTEMPORARY_AGE("EDAD CONTEMPORÁNEA");

    companion object {
        fun parseAge(x: String?): QuestionAge {
            return when (x) {
                OLD.value -> OLD
                MIDDLE_AGE.value -> MIDDLE_AGE
                MODERN_AGE.value -> MODERN_AGE
                CONTEMPORARY_AGE.value -> CONTEMPORARY_AGE
                else -> UNKNOWN
            }
        }
    }
}

enum class QuestionTopic(val value: String) {
    UNKNOWN("DESCONOCIDO"),
    EGYPT("EGIPTO"),
    GREECE("GRECIA"),
    ROME("ROMA"),
    PALEOCHRISTIAN("PALEOCRISTIANO"),
    BYZANTINE("BIZANTINO"),
    PREROMANESQUE("PRERROMÁNICO"),
    ROMANESQUE("ROMÁNICO"),
    GOTHIC("GÓTICO"),
    RENAISSANCE("RENACIMIENTO"),
    MANNERISM("MANIERISMO"),
    BAROQUE("BARROCO"),
    ILLUSTRATION("ILUSTRACIÓN"),
    ENGINEERING("INGENIERÍA"),
    HISTORICISM("HISTORICISMO"),
    ART_NOUVEAU("ART NOUVEAU"),
    VANGUARD("VANGUARDIAS"),
    MODERN("MODERNIDAD"),
    POSTMODERNITY("POSMODERNIDAD"),
    ACTUAL("ACTUAL");

    companion object {
        fun parseTopic(x: String?): QuestionTopic {
            return values().find { it.value.equals(x, true) } ?: UNKNOWN
        }
    }
}
