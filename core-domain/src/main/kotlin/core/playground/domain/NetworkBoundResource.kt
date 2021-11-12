package core.playground.domain

import core.playground.data.ApiEmptyResponse
import core.playground.data.ApiErrorResponse
import core.playground.data.ApiResponse
import core.playground.data.ApiSuccessResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import timber.log.Timber

//TODO delete on first fetch? or ui can hide it? since we do it in transaction, ui won't know until dispatch
// need something delete before fetch?
inline fun <RequestType, ResultType> Flow<ApiResponse<RequestType>>.asNetworkBoundResource(
    query: Flow<ResultType>,
    crossinline shouldFetch: (ResultType?) -> Boolean = { true },
    crossinline processResponse: (
        ApiSuccessResponse<RequestType>,
    ) -> RequestType = ::defaultProcessResponse,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
): Flow<Result<ResultType>> {

    val work: Flow<Result<ResultType>> = transform { response ->
        when (response) {
            is ApiSuccessResponse -> {
                saveFetchResult(processResponse(response))
                emitAll(query.map { Result.Success(it) })
            }
            is ApiEmptyResponse -> {
                emitAll(query.map { Result.Success(it) })
            }
            is ApiErrorResponse -> {
                // TODO: 11/12/2021 Need to throw this as a special error as network error
                Timber.e(response.exception)
                emitAll(query.map { Result.Error(response.exception, it) })
            }
        }
    }

    return flow {
        emit(Result.Loading(null))

        val data = query.flowOn(Dispatchers.Main.immediate).first()

        if (shouldFetch(data)) {
            emitAll(work)
        } else {
            emit(Result.Loading(data))
            emitAll(query.map { Result.Success(it) })
        }
    }
}

fun <RequestType> defaultProcessResponse(response: ApiSuccessResponse<RequestType>) = response.body
