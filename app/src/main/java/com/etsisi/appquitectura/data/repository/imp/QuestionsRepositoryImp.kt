package com.etsisi.appquitectura.data.repository.imp

import com.etsisi.appquitectura.data.datasource.local.QuestionsLocalDataSource
import com.etsisi.appquitectura.data.datasource.remote.QuestionsRemoteDataSource
import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.domain.enums.QuestionLevel
import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.utils.Constants.questions_collection
import kotlin.math.min

class QuestionsRepositoryImp(
    private val remote: QuestionsRemoteDataSource,
    private val local: QuestionsLocalDataSource
) : QuestionsRepository {

    override suspend fun fetchQuestions(): List<QuestionBO>? {
        val localIsEmpty = local.fetchQuestions().isNullOrEmpty()
        if (localIsEmpty) {
            remote.fetchQuestions(questions_collection)?.let {
                addAllQuestions(it.map { it.toDomain() })
                it
            }
        }
        return local.fetchQuestions()
    }

    override suspend fun addAllQuestions(list: List<QuestionBO>) {
        local.addAll(list.map { it.toEntity() })
    }

    override suspend fun deleteAllLocalQuestions() {
        local.deleteAll()
    }

    override suspend fun getGameQuestions(level: QuestionLevel, totalCount: Int, topics: List<QuestionTopic>?): List<QuestionBO>? {
        return local.getQuestionsByTopics(level, topics).shuffled().let { list ->
            list.subList(fromIndex = 0, toIndex = maxOf(0, min(totalCount, list.size-1)))
        }
    }

}