package core.playground.domain

import core.playground.data.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

// TODO delete on first fetch? or ui can hide it? since we do it in transaction, ui won't know until dispatch
// need something delete before fetch?
inline fun <RequestType, ResultType> Flow<Response<RequestType>>.asNetworkBoundResource(
    query: Flow<ResultType>,
    crossinline shouldFetch: (ResultType?) -> Boolean = { true },
    crossinline processResponse: (
        Response.Success<RequestType>,
    ) -> RequestType = ::defaultProcessResponse,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
): Flow<Result<ResultType>> {

    val work: Flow<Result<ResultType>> = transform { response ->
        when (response) {
            is Response.Success -> {
                saveFetchResult(processResponse(response))
                emitAll(query.map { Result.Success(it) })
            }
            is Response.Empty -> {
                emitAll(query.map { Result.Success(it) })
            }
            is Response.Error -> {
                emitAll(query.map { Result.Error(response.exception, it) })
            }
        }
    }

    return flow {
        emit(Result.Loading(null))

        val data = query.flowOn(Dispatchers.Main.immediate).first()
        if (data != null) {
            emit(Result.Loading(data))
        }

        if (shouldFetch(data)) {
            emitAll(work)
        } else {
            emit(Result.Loading(data))
            emitAll(query.map { Result.Success(it) })
        }
    }
}

fun <RequestType> defaultProcessResponse(response: Response.Success<RequestType>) = response.body
