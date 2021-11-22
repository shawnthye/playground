package app.playground.entities.mappers

import api.art.deviant.model.DeviationResult
import app.playground.entities.entries.DeviationEntry
import app.playground.entities.entries.TrackEntity
import core.playground.data.Mapper
import javax.inject.Inject

class TrackDeviationsToEntity @Inject constructor(
    private val deviationResultToEntity: DeviationResultToEntity,
) : Mapper<DeviationResult, Pair<List<TrackEntity>, List<DeviationEntry>>>() {
    override suspend fun map(
        from: DeviationResult,
    ): Pair<List<TrackEntity>, List<DeviationEntry>> {
        val result = deviationResultToEntity(from)

        val track = result.map {
            TrackEntity(
                deviationId = it.deviationId,
                nextPage = from.next_offset ?: 0,
                track = "", // we will set the track in other place
            )
        }

        return Pair(track, result)
    }
}
