package feature.playground.deviant.domain

import app.playground.entities.DeviationEntities
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import feature.playground.deviant.data.DeviantRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDeviantUseCase @Inject constructor(
    private val repository: DeviantRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<String, DeviationEntities>(dispatcher) {

    override fun execute(parameters: String): Flow<Result<DeviationEntities>> = flow {
        emit(
            Result.Success(
                DeviationEntities(
                    "",
                    "",
                    "aaaa",
                    "asdsad",
                    1,
                    1,
                    "popular",
                ),
            ),
        )
    }
}
