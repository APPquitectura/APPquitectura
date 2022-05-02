package com.etsisi.appquitectura.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.concurrent.TimeUnit
import kotlin.math.max

@Parcelize
data class UserGameScoreBO(
    val userQuestions: MutableList<QuestionBO> = mutableListOf(),
    val userAnswer: MutableList<Pair<AnswerBO, Long>> = mutableListOf(),
    var totalTime: Long = 0L
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
        return TimeUnit.MILLISECONDS.toSeconds(totalTime.div(userAnswer.size))
    }
    fun getExperience(): Long {
        var exp = 0L
        userAnswer.forEach {
            if (it.first.correct) {
                exp += it.second
            } else {
                exp -= INCORRECT_ANSWER_SCORE
            }
        }
        exp = max(0, exp)
        return TimeUnit.MILLISECONDS.toSeconds(exp)
    }
}
