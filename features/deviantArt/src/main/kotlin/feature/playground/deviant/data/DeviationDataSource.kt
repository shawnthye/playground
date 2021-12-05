package feature.playground.deviant.data

import api.art.deviant.DeviantArtApi
import app.playground.source.of.truth.database.entities.Deviation
import app.playground.source.of.truth.database.entities.TrackWithDeviation
import app.playground.source.of.truth.mappers.DeviationToEntity
import app.playground.source.of.truth.mappers.TrackDeviationsToEntity
import core.playground.data.Response
import core.playground.data.applyMapper
import app.playground.source.of.truth.database.entities.Track
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
    private val deviantArtApi: DeviantArtApi,
    private val trackDeviationsToEntity: TrackDeviationsToEntity,
    private val deviationToEntity: DeviationToEntity,
) : DeviationDataSource {

    override fun getDeviation(
        id: String,
    ): Flow<Response<Deviation>> = deviantArtApi.deviation(id).applyMapper(deviationToEntity)

    override fun browseDeviations(
        track: Track,
        pageSize: Int,
        nextPage: String?,
    ): Flow<Response<List<TrackWithDeviation>>> {
        return deviantArtApi.browse(
            track = track.toString().lowercase(),
            offset = nextPage,
            limit = pageSize,
        ).applyMapper(trackDeviationsToEntity)
    }
}
