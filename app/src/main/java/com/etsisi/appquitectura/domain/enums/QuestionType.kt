package com.etsisi.appquitectura.domain.enums

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

enum class QuestionTopic(val value: String) {
    ACTUAL("ACTUAL"),
    AUTHOR("ID_AUTOR"),
    ANTIQUITY("ANTIGÜEDAD"),
    ATHENS("ATENAS"),
    ARCHITECTURAL_WORK("ID_OBRA"),
    ART("ARTE"),
    ART_NOUVEAU("ART NOUVEAU"),
    BARCELONA("BARCELONA"),
    BAROQUE("BARROCO"),
    BYZANTINE("BIZANTINO"),
    CENTURY_XV("SIGLO XV"),
    CENTURY_XX("SIGLO XX"),
    CONCEPTS("CONCEPTOS"),
    CONTEMPORARY_AGE("EDAD CONTEMPORÁNEA"),
    EGYPT("EGIPTO"),
    ENGINEERING("INGENIERÍA"),
    GOTHIC("GÓTICO"),
    GREECE("GRECIA"),
    HISTORICISM("HISTORICISMO"),
    ILLUSTRATION("ILUSTRACIÓN"),
    ITALY("ITALIA"),
    MANNERISM("MANIERISMO"),
    MIDDLE_AGE("EDAD MEDIA"),
    MODERN_AGE("EDAD MODERNA"),
    MODERN("MODERNIDAD"),
    OLD("ANTIGÜEDAD"),
    PALEOCHRISTIAN("PALEOCRISTIANO"),
    POSTMODERNITY("POSMODERNIDAD"),
    PREROMANESQUE("PRERROMÁNICO"),
    ROMANESQUE("ROMÁNICO"),
    ROME("ROMA"),
    RENAISSANCE("RENACIMIENTO"),
    SANTIAGO_COMPOSTELA("SANTIAGO DE COMPOSTELA"),
    SPAIN("ESPAÑA"),
    UNKNOWN("DESCONOCIDO"),
    VANGUARD("VANGUARDIAS");

    companion object {
        fun parseTopic(x: String): QuestionTopic {
            return values().find { it.value.equals(x, true) } ?: UNKNOWN
        }
    }
}