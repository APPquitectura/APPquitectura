package com.etsisi.appquitectura.presentation.ui.login.enums

enum class RegisterError(val value: Int) {
    NONE(-1),
    EMPTY_AGE(0),
    EMPTY_NAME(1),
    EMPTY_SURNAME(2),
    EMPTY_ACADEMIC_RECORD(3),
    EMPTY_ACADEMIC_GROUP(4),
    EMPTY_CITY(5),
    EMPTY_EMAIL(6),
    MALFORMED_EMAIL(7),
    EMPTY_PASSWORD(8),
    MALFORMED_PASSWORD(9)
}