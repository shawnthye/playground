package app.playground.ui.debug.domain

import app.playground.ui.debug.data.DebugStorage
import core.playground.IoDispatcher
import core.playground.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class ResetSettingsUseCase @Inject constructor(
    private val storage: DebugStorage,
    private val restartAppUseCase: RestartAppUseCase,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
) : UseCase<Unit, Unit>(ioDispatcher) {

    override suspend fun execute(params: Unit) {
        val currentEnvironment = storage.environment.first()

        storage.clear()

        if (currentEnvironment != DebugStorage.Defaults.Environment) {
            restartAppUseCase(Unit)
        }
    }
}
