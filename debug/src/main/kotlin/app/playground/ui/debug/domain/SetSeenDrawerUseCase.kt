package app.playground.ui.debug.domain

import app.playground.ui.debug.data.DebugStorage
import core.playground.IoDispatcher
import core.playground.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class SetSeenDrawerUseCase @Inject constructor(
    private val debugStorage: DebugStorage,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
) : UseCase<Unit, Unit>(ioDispatcher) {
    override suspend fun execute(params: Unit) {
        val seen = debugStorage.seenDrawer.first()

        if (!seen) {
            debugStorage.seenDrawer()
        }
    }
}
