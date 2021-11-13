package feature.playground.deviant.domain

import app.playground.entities.DeviationEntities
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import feature.playground.deviant.data.DeviantRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularDeviantsUseCase @Inject constructor(
    private val repository: DeviantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, List<DeviationEntities>>(dispatcher) {

    override fun execute(
        parameters: Unit,
    ): Flow<Result<List<DeviationEntities>>> = repository.observePopular()
}
