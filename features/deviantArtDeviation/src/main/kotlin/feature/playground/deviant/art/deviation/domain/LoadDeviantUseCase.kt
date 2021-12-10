package feature.playground.deviant.art.deviation.domain

import app.playground.store.database.entities.Deviation
import app.playground.store.database.entities.DeviationId
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import feature.playground.deviant.art.deviation.data.DeviationRepository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LoadDeviantUseCase @Inject constructor(
    private val repository: DeviationRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<DeviationId, Deviation>(dispatcher) {

    override fun execute(
        params: DeviationId,
    ): Flow<Result<Deviation>> = repository.observeDeviation(params)
}
