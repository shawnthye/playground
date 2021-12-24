package app.playground.ui.debug.domain

import app.playground.ui.debug.data.DebugStorage
import app.playground.ui.debug.data.HttpEngine
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetHttpEngineUseCase @Inject constructor(
    private val storage: DebugStorage,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, HttpEngine>(ioDispatcher) {
    override fun execute(params: Unit) = storage.networkHttpEngine.map { Result.Success(it) }
}
