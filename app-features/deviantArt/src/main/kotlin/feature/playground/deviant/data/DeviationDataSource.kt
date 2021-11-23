package feature.playground.deviant.data

import api.art.deviant.DeviantArt
import app.playground.source.of.truth.database.entities.Deviation
import app.playground.source.of.truth.database.entities.TrackWithDeviation
import app.playground.source.of.truth.mappers.DeviationToEntity
import app.playground.source.of.truth.mappers.TrackDeviationsToEntity
import core.playground.domain.Result
import core.playground.domain.toResult
import feature.playground.deviant.ui.track.Track
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DeviationDataSource {
    fun browseDeviations(
        track: Track,
        nextPage: Int? = null,
    ): Flow<Result<List<TrackWithDeviation>>>

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
        nextPage: Int?,
    ): Flow<Result<List<TrackWithDeviation>>> {
        return api.browse(track = track.toString().lowercase()).toResult(trackDeviationsToEntity)
    }

    override fun getDeviation(id: String): Flow<Result<Deviation>> =
        api.deviation(id)
            .toResult(deviationToEntity)
}
