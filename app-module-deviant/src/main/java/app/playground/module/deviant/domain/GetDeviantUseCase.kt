package app.playground.module.deviant.domain

import app.playground.entities.Deviation
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDeviantUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher) : FlowUseCase<String, Deviation>(coroutineDispatcher) {
    override fun execute(parameters: String): Flow<Result<Deviation>> = flow {
    }
}
