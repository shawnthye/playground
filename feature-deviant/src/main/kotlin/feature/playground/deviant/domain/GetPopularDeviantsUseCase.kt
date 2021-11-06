package feature.playground.deviant.domain

import app.playground.entities.Deviation
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import feature.playground.deviant.data.DeviantRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularDeviantsUseCase @Inject constructor(
    private val repository: DeviantRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, List<Deviation>>(dispatcher) {

    override fun execute(
        parameters: Unit,
    ): Flow<Result<List<Deviation>>> = repository.observePopular()
}
