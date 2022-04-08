package com.etsisi.appquitectura.domain.model

data class UserGameResultBO(
    val userQuestions: MutableList<QuestionBO> = mutableListOf(),
    val userAnswer: MutableList<AnswerBO?> = mutableListOf(),
    var averageUserMillisToAnswer: Long = 0
)  {
    fun getAllCorrectAnswers(): List<AnswerBO?> {
        return userAnswer.filter { it != null && it.correct }
    }
}
