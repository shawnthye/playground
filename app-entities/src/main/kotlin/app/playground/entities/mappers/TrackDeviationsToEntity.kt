package app.playground.entities.mappers

import api.art.deviant.model.DeviationResult
import app.playground.entities.DeviationEntity
import app.playground.entities.TrackDeviationEntity
import core.playground.data.Mapper
import javax.inject.Inject

class TrackDeviationsToEntity @Inject constructor(
    private val deviationResultToEntity: DeviationResultToEntity,
) : Mapper<DeviationResult, Pair<List<TrackDeviationEntity>, List<DeviationEntity>>>() {
    override suspend fun map(
        from: DeviationResult,
    ): Pair<List<TrackDeviationEntity>, List<DeviationEntity>> {
        val result = deviationResultToEntity(from)

        val track = result.map { TrackDeviationEntity(deviationId = it.deviationId, track = "") }

        return Pair(track, result)
    }
}
