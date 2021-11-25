package feature.playground.deviant.data

import app.playground.source.of.truth.database.daos.DeviationDao
import app.playground.source.of.truth.database.daos.DeviationTrackDao
import app.playground.source.of.truth.database.entities.Deviation
import core.playground.data.execute
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

    fun trackPagingSource(track: Track) = deviationTrackDao.pagingSource(track.toString())

    suspend fun suspendFetchTrackAndCache(track: Track, pageSize: Int, nextPage: String?): Boolean {
        val response = deviationDataSource.browseDeviations(track, pageSize, nextPage).execute()

        deviationTrackDao.withTransaction {
            if (nextPage.isNullOrBlank()) {
                deviationTrackDao.deleteTrack(track = track.toString())
            }

            deviationTrackDao.insertIgnore(response.map { it.entry.copy(track = track.toString()) })
            deviationDao.upsert(response.map { it.relation })
        }

        return response.isNotEmpty() && response.last().entry.nextPage.isNullOrBlank().not()
    }

    fun observeDeviation(id: String): Flow<Result<Deviation>> {
        return deviationDataSource.getDeviation(id)
            .asNetworkBoundResult(
                query = deviationDao.observeDeviation(id),
                shouldFetch = { true },
            ) {
                deviationDao.insert(it)
            }
    }
}
