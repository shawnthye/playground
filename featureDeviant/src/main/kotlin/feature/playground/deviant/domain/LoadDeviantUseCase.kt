package feature.playground.deviant.domain

import app.playground.entities.DeviationEntity
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import feature.playground.deviant.data.DeviantRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoadDeviantUseCase @Inject constructor(
    private val repository: DeviantRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<String, DeviationEntity>(dispatcher) {

    override fun execute(parameters: String): Flow<Result<DeviationEntity>> = flow {
        emit(Result.Loading())
        emitAll(repository.observeDeviation(parameters).map { Result.Success(it) })
    }
}
