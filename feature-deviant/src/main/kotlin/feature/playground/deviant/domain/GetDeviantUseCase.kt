package feature.playground.deviant.domain

import app.playground.entities.Deviation
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
) : FlowUseCase<String, Deviation>(dispatcher) {

    override fun execute(parameters: String): Flow<Result<Deviation>> = flow {
        emit(
            Result.Success(
                Deviation(
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
