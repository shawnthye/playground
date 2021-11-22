package feature.playground.deviant.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import api.art.deviant.DeviantArtApi
import app.playground.source.of.truth.database.daos.DeviationDao
import app.playground.source.of.truth.database.daos.DeviationTrackDao
import app.playground.source.of.truth.database.entities.Deviation
import app.playground.source.of.truth.database.entities.TrackWithDeviation
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

    fun observeDeviation(id: String): Flow<Result<Deviation>> = deviationDao
        .observeDeviation(id)
        .asNetworkBoundResult(
            remote = deviationDataSource.getDeviation(id),
            shouldFetch = { true },
        ) {
            deviationDao.insert(it)
        }

    fun observeTrack(track: Track): Flow<Result<List<TrackWithDeviation>>> = deviationTrackDao
        .observeDeviations(track = track.toString(), 100, 0)
        .asNetworkBoundResult(
            remote = deviationDataSource.browseDeviations(track = track),
            shouldFetch = { true },
        ) { response ->
            val tracks = response.first.map { it.copy(track = track.toString()) }
            deviationTrackDao.withTransaction {
                deviationTrackDao.replace(tracks)
                deviationDao.replace(response.second)
            }
        }

    // fun observeTrack(deviationTrack: Track): Flow<PagingData<Deviation>> {
    // }
}

@OptIn(ExperimentalPagingApi::class)
class PageKeyedRemoteMediator(
    private val deviantArtApi: DeviantArtApi,
    private val deviationDao: DeviationDao,
) : RemoteMediator<Int, Deviation>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Deviation>,
    ): MediatorResult {
        state.lastItemOrNull()
        // val loadKey = when (loadType) {
        //     LoadType.REFRESH -> null
        //     LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
        //     LoadType.APPEND -> TODO()
        // }

        TODO("Not yet implemented")
    }
}
