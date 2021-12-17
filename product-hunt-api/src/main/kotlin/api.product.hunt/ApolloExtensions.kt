package api.product.hunt

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.Operation
import core.playground.Reason
import core.playground.data.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun <T : Operation.Data> ApolloCall<T>.asResponse(): Flow<Response<T>> {
    return toFlow().map { response ->
        if (!response.hasErrors()) {
            if (response.data != null) {
                Response.Success(response.dataAssertNoErrors)
            } else {
                Response.Empty
            }
        } else {
            val errorMessages = response.errors?.joinToString("\n") ?: "No error message"
            Response.Error(Reason.HttpError(-1, errorMessages))
        }
    }
}
