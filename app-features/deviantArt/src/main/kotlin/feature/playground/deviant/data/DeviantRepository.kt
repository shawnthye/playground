package feature.playground.deviant.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import app.playground.source.of.truth.database.daos.DeviationDao
import app.playground.source.of.truth.database.daos.DeviationTrackDao
import app.playground.source.of.truth.database.entities.Deviation
import app.playground.source.of.truth.database.entities.TrackWithDeviation
import core.playground.domain.Result
import core.playground.domain.asNetworkBoundResult
import feature.playground.deviant.ui.track.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
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
            deviationTrackDao.withTransaction {
                response.map { it.deviationTrack.copy(track = track.toString()) }.run {
                    deviationTrackDao.replace(this)
                }
                response.map { it.deviation }.run {
                    deviationDao.replace(this)
                }
            }
        }

    @OptIn(ExperimentalPagingApi::class)
    fun observeTrack2(track: Track): Flow<PagingData<TrackWithDeviation>> {
        val pager = Pager(
            config = PagingConfig(pageSize = 100),
            remoteMediator = PageKeyedRemoteMediator(
                deviationDataSource = deviationDataSource,
                deviationTrackDao = deviationTrackDao,
                deviationDao = deviationDao,
                track = track,
            ),
        ) {
            deviationTrackDao.paging(track = track.toString())
        }

        return pager.flow
    }
}

@OptIn(ExperimentalPagingApi::class)
class PageKeyedRemoteMediator(
    private val deviationDataSource: DeviationDataSource,
    private val deviationTrackDao: DeviationTrackDao,
    private val deviationDao: DeviationDao,
    private val track: Track,
) : RemoteMediator<Int, TrackWithDeviation>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TrackWithDeviation>,
    ): MediatorResult {
        state.lastItemOrNull()
        val nextPage = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val last = state.lastItemOrNull() ?: return MediatorResult.Success(
                    endOfPaginationReached = true,
                )

                if (last.deviationTrack.nextPage == 0) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                } else {
                    last.deviationTrack.nextPage
                }
            }
        }

        return when (val result = deviationDataSource.browseDeviations(track, nextPage).last()) {
            is Result.Success -> {
                deviationTrackDao.withTransaction {
                    result.data.map { it.deviationTrack.copy(track = track.toString()) }.run {
                        deviationTrackDao.replace(this)
                    }
                    result.data.map { it.deviation }.run {
                        deviationDao.replace(this)
                    }
                }

                MediatorResult.Success(endOfPaginationReached = false)
            }
            is Result.Error -> MediatorResult.Error(result.throwable)
            else -> MediatorResult.Success(endOfPaginationReached = true)
        }
    }
}
