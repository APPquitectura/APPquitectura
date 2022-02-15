package com.etsisi.quizz.domain.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

abstract class UseCase<Params, Result> {
    abstract suspend fun run(params: Params): Result

    fun invoke(
        scope: CoroutineScope = GlobalScope,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        params: Params,
        onResult: (Result) -> Unit
    ) {
        val job = scope.async(dispatcher) { run(params) }
        scope.launch(Dispatchers.Main) { onResult(job.await()) }
    }
}

abstract class FlowUseCase<in Params, out Result> {

    abstract suspend fun run(params: Params): Flow<Result>

    fun invoke(
        scope: CoroutineScope = GlobalScope,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        params: Params,
        onResult: (Result) -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        val job = scope.async(dispatcher) { run(params) }
        scope.launch (Dispatchers.Main) {
            job.await()
                .catch { onError(it) }
                .collect { onResult(it) }
        }
    }
}