package app.playground.entities.mappers

import api.art.deviant.model.DeviationResult
import app.playground.entities.DeviationEntity
import app.playground.entities.PopularDeviationEntity
import core.playground.data.Mapper
import javax.inject.Inject

class PopularResultToEntity @Inject constructor(
    private val deviationResultToEntity: DeviationResultToEntity,
) : Mapper<DeviationResult, Pair<List<PopularDeviationEntity>, List<DeviationEntity>>>() {
    override suspend fun map(
        from: DeviationResult,
    ): Pair<List<PopularDeviationEntity>, List<DeviationEntity>> {
        val result = deviationResultToEntity(from)

        val popular = result.map { PopularDeviationEntity(deviationId = it.deviationId) }

        return Pair(popular, result)
    }
}
