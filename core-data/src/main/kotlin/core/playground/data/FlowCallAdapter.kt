/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package core.playground.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

/**
 * A Retrofit adapter that converts the Call into a LiveData of ApiResponse.
 * @param <R>
</R> */
class FlowCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, Flow<ApiResponse<R>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): Flow<ApiResponse<R>> {
        return flow {
            emit(
                suspendCancellableCoroutine { continuation ->
                    call.enqueue(
                        object : Callback<R> {
                            override fun onResponse(call: Call<R>, response: Response<R>) {
                                continuation.resumeWith(
                                    Result.success(ApiResponse.create(response)),
                                )
                            }

                            override fun onFailure(call: Call<R>, throwable: Throwable) {
                                // TODO: 11/12/2021 try below for api error handling
                                // continuation.resumeWith(
                                //     Result.failure(throwable),
                                // )
                                continuation.resumeWith(
                                    Result.success(ApiResponse.create(throwable)),
                                )
                            }
                        },
                    )

                    continuation.invokeOnCancellation {
                        call.cancel()
                    }
                },
            )
        }

        // return object : LiveData<ApiResponse<R>>() {
        //     private var started = AtomicBoolean(false)
        //     override fun onActive() {
        //         super.onActive()
        //         if (started.compareAndSet(false, true)) {
        //             call.enqueue(object : Callback<R> {
        //                 override fun onResponse(call: Call<R>, response: Response<R>) {
        //                     postValue(ApiResponse.create(response))
        //                 }
        //
        //                 override fun onFailure(call: Call<R>, throwable: Throwable) {
        //                     postValue(ApiResponse.create(throwable))
        //                 }
        //             })
        //         }
        //     }
        // }
    }
}
