package com.etsisi.appquitectura.data.model.enums

enum class UserGender(val value: String) {
    UNKNOWN(""),
    FEMALE("femenino"),
    MALE("masculino");

    companion object {
        fun parse(gender: String): UserGender {
             return values().find { it.value.equals(gender, true) } ?: UserGender.UNKNOWN
        }
    }
}