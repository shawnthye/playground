// package core.playground.domain
//
// import core.playground.data.ApiEmptyResponse
// import core.playground.data.Response
// import core.playground.data.ApiSuccessResponse
// import core.playground.data.Mapper
// import kotlinx.coroutines.flow.Flow
// import kotlinx.coroutines.flow.transformLatest
//
// //
// // import core.playground.data.ApiEmptyResponse
// // import core.playground.data.ApiErrorResponse
// // import core.playground.data.Response
// // import core.playground.data.ApiSuccessResponse
// // import kotlinx.coroutines.Dispatchers
// // import kotlinx.coroutines.flow.Flow
// // import kotlinx.coroutines.flow.emitAll
// // import kotlinx.coroutines.flow.first
// // import kotlinx.coroutines.flow.flow
// // import kotlinx.coroutines.flow.flowOn
// // import kotlinx.coroutines.flow.map
// // import kotlinx.coroutines.flow.transform
// // import timber.log.Timber
// //
// // // TODO delete on first fetch? or ui can hide it? since we do it in transaction, ui won't know until dispatch
// // // need something delete before fetch?
// // inline fun <ResultType> Flow<Result<ResultType>>.asNetworkBoundResult(
// //     query: Flow<ResultType>,
// //     crossinline shouldFetch: (ResultType?) -> Boolean = { true },
// //     crossinline saveFetchResult: suspend (ResultType) -> Unit,
// // ): Flow<Result<ResultType>> {
// //
// //     val work: Flow<Result<ResultType>> = transform { result ->
// //         when (result) {
// //             is Result.Success -> {
// //                 saveFetchResult(result.data)
// //                 emitAll(query.map { Result.Success(it) })
// //             }
// //             is Result.Error -> {
// //                 emitAll(query.map { Result.Error(it) })
// //             }
// //             is ApiErrorResponse -> {
// //                 // TODO: 11/12/2021 Need to throw this as a special error as network error
// //                 Timber.e(result.exception)
// //                 emitAll(query.map { Result.Error(result.exception, it) })
// //             }
// //         }
// //     }
// //
// //     return flow {
// //         emit(Result.Loading(null))
// //
// //         val data = query.flowOn(Dispatchers.Main.immediate).first()
// //         if (data != null) {
// //             emit(Result.Loading(data))
// //         }
// //
// //         if (shouldFetch(data)) {
// //             emitAll(work)
// //         } else {
// //             emit(Result.Loading(data))
// //             emitAll(query.map { Result.Success(it) })
// //         }
// //     }
// // }
//
// infix fun <
//     RequestType,
//     ResultType,
//     > Flow<Response<RequestType>>.toResult(mapper: Mapper<RequestType, ResultType>): Flow<Result<ResultType>> {
//     return transformLatest { response ->
//         when (response) {
//             is ApiSuccessResponse -> emit(Result.Success(mapper(response.body)))
//             is ApiEmptyResponse -> emit(Result.Error())
//             else -> emit(Result.Error(Throwable("Testing"), null))
//         }
//
//     }
// }
