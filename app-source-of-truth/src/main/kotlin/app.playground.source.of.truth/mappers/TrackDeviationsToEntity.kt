package app.playground.source.of.truth.mappers

import api.art.deviant.model.DeviationResult
import app.playground.source.of.truth.database.entities.DeviationTrack
import app.playground.source.of.truth.database.entities.TrackWithDeviation
import core.playground.data.Mapper
import javax.inject.Inject

class TrackDeviationsToEntity @Inject constructor(
    private val deviationResultToEntity: DeviationResultToEntity,
) : Mapper<DeviationResult, List<TrackWithDeviation>>() {
    override suspend fun map(
        from: DeviationResult,
    ): List<TrackWithDeviation> {
        val deviations = deviationResultToEntity(from)

        val tracks = deviations.map {
            TrackWithDeviation(
                deviationTrack = DeviationTrack(
                    deviationId = it.deviationId,
                    nextPage = from.next_offset ?: 0,
                    track = "", // we will set the deviation when insert to database
                ),
                deviation = it,
            )
        }

        return tracks
    }
}
