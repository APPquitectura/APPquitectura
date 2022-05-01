package com.etsisi.appquitectura.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.concurrent.TimeUnit
import kotlin.math.max

@Parcelize
data class UserGameScoreBO(
    val userQuestions: MutableList<QuestionBO> = mutableListOf(),
    val userAnswer: MutableList<Pair<AnswerBO, Long>> = mutableListOf()
): Parcelable  {

    private companion object {
        //Minus 8 seconds for each incorrect answer
        const val INCORRECT_ANSWER_SCORE = 8000L
    }

    fun getAllCorrectAnswers(): List<AnswerBO> {
        return userAnswer.filter { it.first.correct }.map { it.first }
    }
    fun getAllIncorrectQuestions(): List<QuestionBO> {
        val incorrectQuestionsList = mutableListOf<QuestionBO>()
        userQuestions.mapIndexed { index, questionBO ->
            if (questionBO.answers.find { it.correct } != userAnswer[index].first) {
                incorrectQuestionsList.add(questionBO)
            }
        }
        return incorrectQuestionsList
    }
    fun getAverageTime(): Long {
        var accum = 0L
        userAnswer.forEach {
            accum += it.second
        }
        return TimeUnit.MILLISECONDS.toSeconds(accum.div(userAnswer.size))
    }
    fun getExperience(): Int {
        var exp = 0L
        userAnswer.forEach {
            if (it.first.correct) {
                exp += it.second
            } else {
                exp -= INCORRECT_ANSWER_SCORE
            }
        }
        exp = max(0, exp)
        return TimeUnit.MILLISECONDS.toSeconds(exp).toInt()
    }
}
