package core.playground.domain

import core.playground.data.Mapper
import core.playground.data.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.transform

infix fun <RequestType, ResultType> Flow<Response<RequestType>>.toResult(
    mapper: Mapper<RequestType, ResultType>,
): Flow<Result<ResultType>> {
    return mapLatest { response ->

        when (response) {
            is Response.Success -> {
                Result.Success(mapper(response.body))
            }
            is Response.Error -> Result.Error(response.exception, null)
            // TODO: handle empty later, Result to support empty? or treat it as Error too?
            is Response.Empty -> throw NotImplementedError()
        }

    }
}

// TODO how to delete on first fetch? or ui can hide it? since we do it in transaction, ui won't know until dispatch
// need something delete before fetch?
inline fun <ResultType, RemoteType> Flow<ResultType>.asNetworkBoundResult(
    remote: Flow<Result<RemoteType>>,
    crossinline shouldFetch: suspend (cache: ResultType?) -> Boolean = { true },
    crossinline saveRemote: suspend (remote: RemoteType) -> Unit,
): Flow<Result<ResultType>> {

    val cache = this

    val work: Flow<Result<ResultType>> = remote.transform { result ->
        when (result) {
            is Result.Success -> {
                saveRemote(result.data)
                emitAll(cache.map { Result.Success(it) })
            }
            is Result.Error -> {
                emitAll(cache.map { Result.Error(result.throwable, it) })
            }
            else -> throw IllegalStateException("impossible to reach here")
        }
    }

    return flow {
        emit(Result.Loading(null))

        val data = cache.flowOn(Dispatchers.Main.immediate).first()
        if (data != null) {
            emit(Result.Loading(data))
        }

        if (shouldFetch(data)) {
            emitAll(work)
        } else {
            emit(Result.Loading(data))
            emitAll(cache.map { Result.Success(it) })
        }
    }
}
