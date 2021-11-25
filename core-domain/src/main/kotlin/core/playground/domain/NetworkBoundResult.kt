package core.playground.domain

import core.playground.data.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import timber.log.Timber

inline fun <T> Flow<Response<T>>.asNetworkBoundResult(
    query: Flow<T>,
    crossinline shouldFetch: suspend (cache: T?) -> Boolean = { true },
    crossinline saveRemote: suspend (remote: T) -> Unit,
): Flow<Result<T>> {

    val work: Flow<Result<T>> = transform { response ->
        when (response) {
            is Response.Success -> {
                saveRemote(response.body)
                emitAll(query.map { Result.Success(it) })
            }
            is Response.Empty -> emitAll(query.map { Result.Success(it) })
            is Response.Error -> {
                Timber.e(response.exception)
                emitAll(query.map { Result.Error(response.exception, it) })
            }
        }
    }

    return flow {
        emit(Result.Loading(null))

        val data = query.first()
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
