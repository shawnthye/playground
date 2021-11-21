package feature.playground.deviant.data

import app.playground.core.data.daos.DeviationDao
import app.playground.entities.DeviationEntity
import core.playground.domain.Result
import core.playground.domain.asNetworkBoundResult
import feature.playground.deviant.ui.track.Track
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviantRepository @Inject constructor(
    private val deviationDataSource: DeviationDataSource,
    private val deviationDao: DeviationDao,
) {

    fun observeDeviation(id: String): Flow<Result<DeviationEntity>> = deviationDao
        .observeDeviation(id)
        .asNetworkBoundResult(
            remote = deviationDataSource.getDeviation(id),
            shouldFetch = { true },
        ) {
            deviationDao.insert(it)
        }

    fun observePopular(
        track: Track,
    ): Flow<Result<List<DeviationEntity>>> = deviationDao
        .observeTrackDeviations(track = track.toString())
        .asNetworkBoundResult(
            remote = deviationDataSource.browseDeviations(track),
            shouldFetch = { true },
        ) { response ->
            val tracks = response.first.map { it.copy(track = track.toString()) }
            deviationDao.insertTracks(tracks, response.second)
        }
}
