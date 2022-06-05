package com.etsisi.appquitectura.data.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.etsisi.appquitectura.data.model.enums.ScoreLevel
import com.etsisi.appquitectura.domain.model.ScoreBO

@Entity(tableName = "scores")
data class ScoreEntity(
    @PrimaryKey @ColumnInfo(name = "level") val level: String,
    val value: Float
) {
    fun toDomain() = ScoreBO(
        level = ScoreLevel.parseToLevel(level),
        value = value
    )
}