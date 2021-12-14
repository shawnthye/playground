package feature.playground.demos.error.domain

import api.art.deviant.DeviantArt
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import core.playground.domain.toResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class Response204UseCase @Inject constructor(
    private val deviantArt: DeviantArt,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, Unit>(coroutineDispatcher) {
    override fun execute(params: Unit): Flow<Result<Unit>> = deviantArt
        .api
        .url("https://clients3.google.com/generate_204")
        .toResult()
}
