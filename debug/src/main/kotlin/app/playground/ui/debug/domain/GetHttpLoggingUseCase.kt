package app.playground.ui.debug.domain

import app.playground.ui.debug.data.DebugStorage
import app.playground.ui.debug.data.HttpLogging
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetHttpLoggingUseCase @Inject constructor(
    private val storage: DebugStorage,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, HttpLogging>(ioDispatcher) {

    override fun execute(params: Unit): Flow<Result<HttpLogging>> = storage.httpLoggingLevel.map {
        Result.Success(it)
    }
}
