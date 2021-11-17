package feature.playground.deviant.domain

import app.playground.entities.DeviationEntity
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import feature.playground.deviant.data.DeviantRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadPopularDeviantsUseCase @Inject constructor(
    private val repository: DeviantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, List<DeviationEntity>>(dispatcher) {

    override fun execute(
        parameters: Unit,
    ): Flow<Result<List<DeviationEntity>>> = repository.observePopular()
}
