package app.playground.ui.debug.domain

import app.playground.ui.debug.data.DebugStorage
import app.playground.ui.debug.data.HttpEngine
import core.playground.IoDispatcher
import core.playground.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class SetHttpEngineUseCase @Inject constructor(
    private val storage: DebugStorage,
    private val restartAppUseCase: RestartAppUseCase,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
) : UseCase<HttpEngine, Unit>(ioDispatcher) {

    override suspend fun execute(params: HttpEngine) {
        storage.networkHttpEngine(params)
        restartAppUseCase(Unit)
    }
}
