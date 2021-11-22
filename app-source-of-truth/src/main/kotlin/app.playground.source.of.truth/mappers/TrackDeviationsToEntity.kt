package app.playground.source.of.truth.mappers

import api.art.deviant.model.DeviationResult
import app.playground.source.of.truth.database.entities.Deviation
import app.playground.source.of.truth.database.entities.DeviationTrack
import core.playground.data.Mapper
import javax.inject.Inject

class TrackDeviationsToEntity @Inject constructor(
    private val deviationResultToEntity: DeviationResultToEntity,
) : Mapper<DeviationResult, Pair<List<DeviationTrack>, List<Deviation>>>() {
    override suspend fun map(
        from: DeviationResult,
    ): Pair<List<DeviationTrack>, List<Deviation>> {
        val result = deviationResultToEntity(from)

        val track = result.map {
            DeviationTrack(
                deviationId = it.deviationId,
                nextPage = from.next_offset ?: 0,
                track = "", // we will set the deviation when insert to database
            )
        }

        return Pair(track, result)
    }
}
