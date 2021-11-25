package feature.playground.deviant.data

import api.art.deviant.DeviantArt
import app.playground.source.of.truth.database.entities.Deviation
import app.playground.source.of.truth.database.entities.TrackWithDeviation
import app.playground.source.of.truth.mappers.DeviationToEntity
import app.playground.source.of.truth.mappers.TrackDeviationsToEntity
import core.playground.data.Response
import core.playground.data.applyMapper
import feature.playground.deviant.ui.track.Track
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DeviationDataSource {

    fun getDeviation(id: String): Flow<Response<Deviation>>

    fun browseDeviations(
        track: Track,
        pageSize: Int,
        nextPage: String? = null,
    ): Flow<Response<List<TrackWithDeviation>>>
}

internal class DeviationDataSourceImpl @Inject constructor(
    private val deviantArt: DeviantArt,
    private val trackDeviationsToEntity: TrackDeviationsToEntity,
    private val deviationToEntity: DeviationToEntity,
) : DeviationDataSource {

    private val api by lazy { deviantArt.api }

    override fun getDeviation(
        id: String,
    ): Flow<Response<Deviation>> = api.deviation(id).applyMapper(deviationToEntity)

    override fun browseDeviations(
        track: Track,
        pageSize: Int,
        nextPage: String?,
    ): Flow<Response<List<TrackWithDeviation>>> {
        return api.browse(
            track = track.toString().lowercase(),
            offset = nextPage,
            limit = pageSize,
        ).applyMapper(trackDeviationsToEntity)
    }
}
