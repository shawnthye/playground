package app.playground.ui.debug.domain

import app.playground.ui.debug.data.DebugEnvironment
import app.playground.ui.debug.data.DebugStorage
import core.playground.IoDispatcher
import core.playground.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class SetDebugEnvironmentUseCase @Inject constructor(
    private val storage: DebugStorage,
    private val restartAppUseCase: RestartAppUseCase,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
) : UseCase<DebugEnvironment, Unit>(ioDispatcher) {

    override suspend fun execute(params: DebugEnvironment) {
        storage.environment(params)
        restartAppUseCase(Unit)
    }
}
