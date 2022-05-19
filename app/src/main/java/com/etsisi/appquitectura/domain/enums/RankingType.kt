package com.etsisi.appquitectura.domain.enums

import com.etsisi.appquitectura.presentation.utils.EMPTY

enum class RankingType(val field: String){
    UNKOWN(String.EMPTY),
    GENERAL("general"),
    WEEKLY("weekly");

    companion object {
        fun parse(value: String): RankingType {
            return values().find { it.field.equals(value, true) } ?: UNKOWN
        }
    }
}
