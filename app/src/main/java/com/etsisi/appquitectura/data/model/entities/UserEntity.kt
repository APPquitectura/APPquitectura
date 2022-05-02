package com.etsisi.appquitectura.data.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.etsisi.appquitectura.domain.model.QuestionSubject
import com.etsisi.appquitectura.domain.model.UserBO

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    val email: String,
    val name: String,
    val subject: String,
    val gameExperience: Long,
    val totalQuestionsAnswered: Int,
    val totalCorrectQuestionsAnswered: Int
) {
    fun toDomain() = UserBO(
        id = id,
        name = name,
        email = email,
        subject = QuestionSubject.parseSubject(subject),
        gameExperience = gameExperience,
        totalQuestionsAnswered = totalQuestionsAnswered,
        totalCorrectQuestionsAnswered = totalCorrectQuestionsAnswered
    )
}