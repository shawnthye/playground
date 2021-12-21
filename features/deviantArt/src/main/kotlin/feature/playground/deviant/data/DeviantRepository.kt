package feature.playground.deviant.data

import app.playground.store.database.daos.DeviationDao
import app.playground.store.database.daos.DeviationTrackDao
import app.playground.store.database.entities.Deviation
import app.playground.store.database.entities.Track
import core.playground.data.execute
import core.playground.domain.Result
import core.playground.domain.asNetworkBoundResult
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class DeviantRepository @Inject constructor(
    private val deviationDao: DeviationDao,
    private val deviationTrackDao: DeviationTrackDao,
    private val deviationDataSource: DeviationDataSource,
) {

    fun trackPagingSource(track: Track) = deviationTrackDao.pagingSource(track.toString())

    suspend fun fetchTrack(track: Track, pageSize: Int, nextPage: String?): Boolean {
        val request = deviationDataSource.browseDeviations(track, pageSize, nextPage)

        val body = request.execute() ?: return true

        deviationTrackDao.withTransaction {
            if (nextPage.isNullOrBlank()) {
                Timber.i("Delete track")
                deviationTrackDao.deleteTrack(track = track.toString())
            }

            deviationDao.upsert(body.map { it.deviation })
            deviationTrackDao.replace(body.map { it.entry.copy(track = track) })
        }

        return body.lastOrNull()?.entry?.nextPage.isNullOrBlank()
    }

    fun observeDeviation(id: String): Flow<Result<Deviation>> {
        return deviationDataSource.getDeviation(id).asNetworkBoundResult(
            query = deviationDao.observeDeviation(id),
            shouldFetch = { true },
        ) {
            deviationDao.replace(it)
        }
    }

    suspend fun updateDeviation(deviation: Deviation) {
        deviationDao.update(deviation)
    }
}
