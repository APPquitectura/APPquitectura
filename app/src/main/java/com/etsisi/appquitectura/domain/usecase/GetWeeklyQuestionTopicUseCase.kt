package com.etsisi.appquitectura.domain.usecase

import com.etsisi.appquitectura.domain.enums.QuestionTopic
import java.util.Calendar
import kotlin.random.Random

class GetWeeklyQuestionTopicUseCase {
    data class Params(
        val topics: List<QuestionTopic>
    )

    fun invoke(params: Params): QuestionTopic {
         return with(params) {
             topics[Random(seed = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)).nextInt(topics.size)]
         }
    }
}