package feature.playground.deviant.art.track.data

import api.art.deviant.DeviantArtApi
import app.playground.store.database.entities.Track
import app.playground.store.database.entities.TrackWithDeviation
import app.playground.store.mappers.TrackDeviationsToEntity
import core.playground.data.Response
import core.playground.data.withMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DeviationDataSource {

    fun browseDeviations(
        track: Track,
        pageSize: Int,
        nextPage: String? = null,
    ): Flow<Response<List<TrackWithDeviation>>>
}

internal class DeviationDataSourceImpl @Inject constructor(
    private val deviantArtApi: DeviantArtApi,
    private val trackDeviationsToEntity: TrackDeviationsToEntity,
) : DeviationDataSource {

    override fun browseDeviations(
        track: Track,
        pageSize: Int,
        nextPage: String?,
    ): Flow<Response<List<TrackWithDeviation>>> {
        return deviantArtApi.browse(
            track = track.toString().lowercase(),
            offset = nextPage,
            limit = pageSize,
        ).withMapper(trackDeviationsToEntity)
    }
}
