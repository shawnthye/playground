package api.product.hunt

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.coroutines.await
import core.playground.Reason
import core.playground.data.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal fun <T> ApolloCall<T>.asResponse(): Flow<Response<T>> {
    return flow {
        val response = await()

        if (!response.hasErrors()) {
            if (response.data != null) {
                emit(Response.Success(response.data!!))
            } else {
                emit(Response.Empty)
            }
        } else {
            val errorMessages = response.errors?.joinToString("\n") ?: "No error message"
            emit(Response.Error(Reason.HttpError(-1, errorMessages)))
        }
    }
}
