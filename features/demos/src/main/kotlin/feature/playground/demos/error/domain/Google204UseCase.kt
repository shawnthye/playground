package feature.playground.demos.error.domain

import core.playground.IoDispatcher
import core.playground.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class Google204UseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
) : UseCase<Unit, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameters: Unit) {
        TODO("Not yet implemented")
    }
}
