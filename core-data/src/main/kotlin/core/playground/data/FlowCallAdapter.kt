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

                    continuation.invokeOnCancellation {
                        call.cancel()
                    }
                },
            )
        }
    }
}

// /**
//  * Here we use google to check if the connect too slow
//  */
// private fun Retrofit.finalizeError(original: Throwable): Throwable {
//     val client = (callFactory() as OkHttpClient)
//         .newBuilder()
//         .callTimeout(Duration.ofSeconds(10))
//         .connectTimeout(Duration.ofSeconds(5))
//         .retryOnConnectionFailure(true)
//         .apply {
//             /**
//              * clear all the interceptor, since we doesn't need it
//              * we only need other setting eg: DNS,
//              * so that we can use the closet environment to check connection
//              */
//             interceptors().clear()
//         }
//         .build()
//
//     return try {
//         client.newCall(GENERATE_204_REQUEST).execute()
//         // no problem, return the original
//         original
//     } catch (e: IOException) {
//         return Reason.Connection(original)
//     } catch (e: Throwable) {
//         /**
//          * something else, return the original too
//          * TODO: a way to return both? in case the when checking connection?
//          */
//         original
//     }
// }
//
// private val GENERATE_204_REQUEST by lazy {
//     Request.Builder()
//         .url("https://clients3.google.com/generate_204")
//         .method("HEAD", null)
//         .header("Cache-Control", "no-cache")
//         .cacheControl(CacheControl.FORCE_NETWORK)
//         .build()
// }
