package com.etsisi.appquitectura.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserGameScoreBO(
    val userQuestions: MutableList<QuestionBO> = mutableListOf(),
    val userAnswer: MutableList<AnswerBO?> = mutableListOf(),
    var averageUserMillisToAnswer: Long = 0
): Parcelable  {
    fun getAllCorrectAnswers(): List<AnswerBO?> {
        return userAnswer.filter { it != null && it.correct }
    }
}
