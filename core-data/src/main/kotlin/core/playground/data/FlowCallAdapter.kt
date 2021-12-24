package core.playground.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import java.lang.reflect.Type

/**
 * A Retrofit adapter that converts the Call into a LiveData of Response.
 * @param <R>
</R> */
class FlowCallAdapter<R>(
    private val responseType: Type,
) : CallAdapter<R, Flow<Response<R>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): Flow<Response<R>> {
        return flow {
            emit(
                suspendCancellableCoroutine { continuation ->
                    call.let {
                        if (!it.isExecuted) it else it.clone()
                    }.enqueue(
                        object : Callback<R> {
                            override fun onResponse(
                                call: Call<R>,
                                response: retrofit2.Response<R>,
                            ) {
                                continuation.resumeWith(
                                    Result.success(
                                        Response.create(response),
                                    ),
                                )
                            }

                            override fun onFailure(call: Call<R>, throwable: Throwable) {
                                continuation.resumeWith(Result.success(Response.create(throwable)))
                            }
                        },
                    )

                    continuation.invokeOnCancellation { call.cancel()
                    }
                },
            )
        }
    }
}
