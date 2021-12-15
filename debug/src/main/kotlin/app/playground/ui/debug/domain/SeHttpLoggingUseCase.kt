package app.playground.ui.debug.domain

import app.playground.ui.debug.data.DebugStorage
import app.playground.ui.debug.data.HttpLogging
import core.playground.IoDispatcher
import core.playground.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class SeHttpLoggingUseCase @Inject constructor(
    private val debugStorage: DebugStorage,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
) : UseCase<HttpLogging, Unit>(ioDispatcher) {
    override suspend fun execute(params: HttpLogging) {
        debugStorage.httpLoggingLevel(params)
    }
}
