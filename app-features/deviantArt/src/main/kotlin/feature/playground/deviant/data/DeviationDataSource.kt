package feature.playground.deviant.data

import api.art.deviant.DeviantArt
import app.playground.source.of.truth.database.entities.Deviation
import app.playground.source.of.truth.database.entities.DeviationTrack
import app.playground.source.of.truth.mappers.DeviationToEntity
import app.playground.source.of.truth.mappers.TrackDeviationsToEntity
import core.playground.domain.Result
import core.playground.domain.toResult
import feature.playground.deviant.ui.track.Track
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DeviationDataSource {
    fun browseDeviations(track: Track): Flow<Result<Pair<List<DeviationTrack>, List<Deviation>>>>
    fun getDeviation(id: String): Flow<Result<Deviation>>
}

internal class DeviationDataSourceImpl @Inject constructor(
    private val deviantArt: DeviantArt,
    private val trackDeviationsToEntity: TrackDeviationsToEntity,
    private val deviationToEntity: DeviationToEntity,
) : DeviationDataSource {

    private val api by lazy { deviantArt.api }

    override fun browseDeviations(
        track: Track,
    ): Flow<Result<Pair<List<DeviationTrack>, List<Deviation>>>> {
        return api.browse(track = track.toString().lowercase()).toResult(trackDeviationsToEntity)
    }

    override fun getDeviation(id: String): Flow<Result<Deviation>> =
        api.deviation(id)
            .toResult(deviationToEntity)
}
