package com.etsisi.appquitectura.domain.model

import android.os.Parcelable
import com.etsisi.appquitectura.domain.enums.QuestionLevel
import com.etsisi.appquitectura.domain.enums.RankingType
import com.etsisi.appquitectura.presentation.utils.penultimate
import kotlinx.parcelize.Parcelize
import java.util.concurrent.TimeUnit

@Parcelize
data class UserGameScoreBO(
    val userQuestions: MutableList<QuestionBO> = mutableListOf(),
    val userAnswer: MutableList<Pair<AnswerBO, Long>> = mutableListOf(),
    val rankingType: RankingType? = null,
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
    fun getRankingPoints(): Int {
        var points = 0L
        userAnswer.forEach {
            if (it.first.correct) {
                points += it.second
            } else {
                points -= INCORRECT_ANSWER_SCORE
            }
        }
        return TimeUnit.MILLISECONDS.toSeconds(points).toInt()
    }
    fun getExperience(): Long {
        var exp = 0L
        userAnswer.forEach {
            if (it.first.correct) {
                exp += it.second
            }
        }
        return TimeUnit.MILLISECONDS.toSeconds(exp)
    }
    fun getLevelOfNextQuestion(): QuestionLevel {
        var lastQuestionsCorrect = 0
        var i = userAnswer.size - 1

        while (i >= 0  && i > userAnswer.size - 3 && userAnswer[i].first.correct) {
            lastQuestionsCorrect++
            i--
        }
        return when {
            lastQuestionsCorrect >= 2 -> QuestionLevel.DIFFICULT
            lastQuestionsCorrect == 1 -> QuestionLevel.NORMAL
            else -> QuestionLevel.EASY
        }
    }
}
