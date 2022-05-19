package com.etsisi.appquitectura.domain.enums

import android.os.Parcelable
import com.etsisi.appquitectura.presentation.utils.EMPTY
import kotlinx.parcelize.Parcelize

@Parcelize
enum class RankingType(val field: String) : Parcelable {
    UNKOWN(String.EMPTY),
    GENERAL("general"),
    WEEKLY("weekly");

    companion object {
        fun parse(value: String): RankingType {
            return values().find { it.field.equals(value, true) } ?: UNKOWN
        }
    }
}
