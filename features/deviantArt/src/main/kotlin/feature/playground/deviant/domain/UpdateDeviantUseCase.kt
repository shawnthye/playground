package feature.playground.deviant.domain

import app.playground.store.database.entities.Deviation
import core.playground.IoDispatcher
import core.playground.domain.UseCase
import feature.playground.deviant.data.DeviantRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UpdateDeviantUseCase @Inject constructor(
    private val repository: DeviantRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Deviation, Unit>(dispatcher) {

    override suspend fun execute(params: Deviation) = repository.updateDeviation(params)
}
