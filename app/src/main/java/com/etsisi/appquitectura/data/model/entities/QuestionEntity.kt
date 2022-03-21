package com.etsisi.appquitectura.data.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.etsisi.appquitectura.domain.model.QuestionBO

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    val title: String
) {
    fun toDomain() = QuestionBO(
        id = id,
        title = title
    )
}
