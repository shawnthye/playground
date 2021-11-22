package app.playground.entities.mappers

import api.art.deviant.model.DeviationResult
import app.playground.entities.entities.Deviation
import app.playground.entities.entities.DeviationTrack
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
