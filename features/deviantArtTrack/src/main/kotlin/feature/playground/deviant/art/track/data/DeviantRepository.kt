package feature.playground.deviant.art.track.data

import app.playground.store.database.daos.DeviationDao
import app.playground.store.database.daos.DeviationTrackDao
import app.playground.store.database.entities.Track
import core.playground.data.execute
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

        val body = request.execute() ?: return false

        deviationTrackDao.withTransaction {
            if (nextPage.isNullOrBlank()) {
                Timber.i("Delete track")
                deviationTrackDao.deleteTrack(track = track.toString())
            }

            deviationTrackDao.replace(body.map { it.entry.copy(track = track) })
            deviationDao.upsert(body.map { it.relation })
        }

        return body.isNotEmpty() && body.last().entry.nextPage.isNullOrBlank().not()
    }
}
