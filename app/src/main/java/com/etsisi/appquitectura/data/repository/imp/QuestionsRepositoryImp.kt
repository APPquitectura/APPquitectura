package com.etsisi.appquitectura.data.repository.imp

import com.etsisi.appquitectura.data.datasource.local.QuestionsLocalDataSource
import com.etsisi.appquitectura.data.datasource.remote.QuestionsRemoteDataSource
import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.domain.model.QuestionSubject

class QuestionsRepositoryImp(
    private val remote: QuestionsRemoteDataSource,
    private val local: QuestionsLocalDataSource
) : QuestionsRepository {

    override suspend fun fetchQuestions(collection: String, questionSubject: QuestionSubject): List<QuestionBO>? {
        val localIsEmpty = local.fetchQuestions().isNullOrEmpty()
        if (localIsEmpty) {
            remote.fetchQuestions(collection, questionSubject)?.let {
                addAllQuestions(it)
                it
            }
        }
        return local.fetchQuestions()
    }

    override suspend fun addAllQuestions(list: List<QuestionBO>) {
        local.addAll(list)
    }

    override suspend fun deleteAllLocalQuestions() {
        local.deleteAll()
    }

}