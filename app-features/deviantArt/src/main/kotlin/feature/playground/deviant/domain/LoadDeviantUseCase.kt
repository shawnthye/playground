package feature.playground.deviant.domain

import app.playground.entities.entries.DeviationEntry
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
) : FlowUseCase<String, DeviationEntry>(dispatcher) {

    override fun execute(
        parameters: String,
    ): Flow<Result<DeviationEntry>> = repository.observeDeviation(parameters)
}
