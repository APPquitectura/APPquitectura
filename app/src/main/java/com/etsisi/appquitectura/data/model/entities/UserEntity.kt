package com.etsisi.appquitectura.data.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.etsisi.appquitectura.data.model.enums.UserGender
import com.etsisi.appquitectura.domain.enums.QuestionSubject
import com.etsisi.appquitectura.domain.model.UserBO

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    val email: String,
    val name: String,
    val subject: String,
    val gender: String,
    val surname: String,
    val academicRecord: String,
    val academicGroup: String,
    val city: String,
    val gameExperience: Long,
    val totalQuestionsAnswered: Int,
    val totalCorrectQuestionsAnswered: Int
) {
    fun toDomain() = UserBO(
        id = id,
        name = name,
        email = email,
        course = QuestionSubject.parseSubject(subject),
        gender = UserGender.parse(gender),
        surname = surname,
        city = city,
        academicGroup = academicGroup,
        academicRecord = academicRecord,
        gameExperience = gameExperience,
        totalQuestionsAnswered = totalQuestionsAnswered,
        totalCorrectQuestionsAnswered = totalCorrectQuestionsAnswered
    )
}