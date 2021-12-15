package app.playground.ui.debug.domain

import app.playground.ui.debug.data.DebugStorage
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetSeenDrawerUseCase @Inject constructor(
    private val debugStorage: DebugStorage,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, Boolean>(ioDispatcher) {
    override fun execute(params: Unit): Flow<Result<Boolean>> = debugStorage.seenDrawer.map {
        Result.Success(it)
    }
}
