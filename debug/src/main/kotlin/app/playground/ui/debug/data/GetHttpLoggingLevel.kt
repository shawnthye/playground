package app.playground.ui.debug.data

import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

// internal class GetHttpLoggingLevel @Inject constructor(
//     private val preferenceStorage: DebugPreferenceStorage,
//     @IoDispatcher dispatcher: CoroutineDispatcher,
// ) : FlowUseCase<Unit, HttpLoggingInterceptor.Level>(
//     dispatcher,
// ) {
//     override fun execute(params: Unit): Flow<Result<HttpLoggingInterceptor.Level>> = flow {
//         emit(preferenceStorage.httpLoggingLevel.first())
//     }
// }
