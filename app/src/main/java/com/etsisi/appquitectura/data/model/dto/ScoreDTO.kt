package com.etsisi.appquitectura.data.model.dto

import com.etsisi.appquitectura.data.model.enums.ScoreLevel
import com.etsisi.appquitectura.domain.model.ScoreBO

data class ScoreDTO (
    val levels: Map<String, Float> = mapOf()
) {
    fun toDomain(): List<ScoreBO> {
        return levels.keys.mapIndexed { index, s ->
            ScoreBO(
                level = ScoreLevel.parseToLevel(s),
                value = levels[s] ?: 0F
            )
        }
    }
}