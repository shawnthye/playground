package app.playground.ui.debug.domain

import app.playground.ui.debug.data.DebugEnvironment
import app.playground.ui.debug.data.DebugStorage
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetDebugEnvironmentUseCase @Inject constructor(
    private val storage: DebugStorage,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, DebugEnvironment>(ioDispatcher) {

    override fun execute(params: Unit): Flow<Result<DebugEnvironment>> = storage.environment.map {
        Result.Success(it)
    }
}
