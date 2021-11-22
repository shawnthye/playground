package feature.playground.deviant.domain

import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import feature.playground.deviant.data.DeviantRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadDeviantUseCase @Inject constructor(
    private val repository: DeviantRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<String, app.playground.source.of.truth.database.entities.Deviation>(dispatcher) {

    override fun execute(
        parameters: String,
    ): Flow<Result<app.playground.source.of.truth.database.entities.Deviation>> =
        repository.observeDeviation(parameters)
}
