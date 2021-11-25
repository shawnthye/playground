package feature.playground.deviant.data

import app.playground.source.of.truth.database.daos.DeviationDao
import app.playground.source.of.truth.database.daos.DeviationTrackDao
import app.playground.source.of.truth.database.entities.Deviation
import app.playground.source.of.truth.database.entities.TrackWithDeviation
import core.playground.data.Response
import core.playground.data.runOnSucceeded
import core.playground.domain.Result
import core.playground.domain.asNetworkBoundResult
import feature.playground.deviant.ui.track.Track
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviantRepository @Inject constructor(
    private val deviationDao: DeviationDao,
    private val deviationTrackDao: DeviationTrackDao,
    private val deviationDataSource: DeviationDataSource,
) {

    fun observeDeviation2(
        id: String,
    ): Flow<Result<Deviation>> = deviationDataSource.getDeviation(id)
        .asNetworkBoundResult(
            query = deviationDao.observeDeviation(id),
            shouldFetch = { true },
        ) {
            deviationDao.insert(it)
        }

    fun trackPagingSource(track: Track) = deviationTrackDao.pagingSource(track.toString())

    fun fetchTrackAndCache(
        track: Track,
        pageSize: Int,
        nextPage: String?,
    ): Flow<Response<List<TrackWithDeviation>>> {
        return deviationDataSource.browseDeviations(
            track = track,
            pageSize = pageSize,
            nextPage = nextPage,
        ).runOnSucceeded {
            deviationTrackDao.withTransaction {
                deviationTrackDao.insertIgnore(it.map { it.entry.copy(track = track.toString()) })
                deviationDao.upsert(it.map { it.relation })
            }
        }
    }
}
