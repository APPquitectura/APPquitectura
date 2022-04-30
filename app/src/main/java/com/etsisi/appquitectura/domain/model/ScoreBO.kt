package com.etsisi.appquitectura.domain.model

import com.etsisi.appquitectura.data.model.entities.ScoreEntity
import com.etsisi.appquitectura.data.model.enums.ScoreLevel

data class ScoreBO(
    val level: ScoreLevel,
    val value: Float
) {
    fun toEntity() = ScoreEntity(
        level = level.value,
        value = value
    )
}