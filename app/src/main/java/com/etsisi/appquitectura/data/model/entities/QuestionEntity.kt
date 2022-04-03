package com.etsisi.appquitectura.data.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.etsisi.appquitectura.domain.model.QuestionAge
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.domain.model.QuestionLevel
import com.etsisi.appquitectura.domain.model.QuestionTopic
import com.etsisi.appquitectura.domain.model.QuestionSubject

@Entity(tableName = "questions")
data class QuestionEntity(
        @PrimaryKey @ColumnInfo(name = "id") val id: String,
        val title: String,
        val subject: Int,
        val level: Int,
        val age: Int,
        val topic: Int
) {
    fun toDomain() = QuestionBO(
            id = id,
            title = title,
            subject = QuestionSubject.parseQuest(subject),
            level = QuestionLevel.parseLevel(level),
            age = QuestionAge.parseAge(age),
            topic = QuestionTopic.parseTopic(topic)
    )
}
