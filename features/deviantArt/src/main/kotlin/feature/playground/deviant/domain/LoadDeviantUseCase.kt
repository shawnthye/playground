package feature.playground.deviant.domain

import app.playground.store.database.entities.Deviation
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
) : FlowUseCase<String, Deviation>(dispatcher) {

    override fun execute(
        params: String,
    ): Flow<Result<Deviation>> = repository.observeDeviation(params)
}
