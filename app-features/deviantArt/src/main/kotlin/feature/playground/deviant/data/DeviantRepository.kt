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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import timber.log.Timber
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

    @OptIn(ExperimentalPagingApi::class)
    fun observeTrack2(track: Track): Flow<PagingData<TrackWithDeviation>> {
        val pager = Pager(
            config = PagingConfig(
                pageSize = 12,
                initialLoadSize = 12,
                enablePlaceholders = true,
            ),
            remoteMediator = PageKeyedRemoteMediator(
                deviationDataSource = deviationDataSource,
                deviationTrackDao = deviationTrackDao,
                deviationDao = deviationDao,
                track = track,
            ),
            pagingSourceFactory = { deviationTrackDao.paging(track = track.toString()) },
        )

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
        val nextPage = when (loadType) {
            LoadType.REFRESH -> {
                Timber.i("$loadType, nextPage: ${state.lastItemOrNull()?.deviationTrack?.nextPage}")
                null
            }
            LoadType.PREPEND -> {
                Timber.i("$loadType, nextPage: ${state.lastItemOrNull()?.deviationTrack?.nextPage}")
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                if (state.isEmpty()) {
                    // database is empty
                    null
                } else {
                    Timber.i("$loadType, nextPage: ${state.lastItemOrNull()?.deviationTrack?.nextPage}")
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
        }

        delay(1500)
        Timber.i("$loadType fetching data with nextPage $nextPage")
        return when (val result = deviationDataSource.browseDeviations(
            track = track,
            pageSize = when (loadType) {
                LoadType.REFRESH -> state.config.initialLoadSize
                else -> state.config.pageSize
            },
            nextPage = nextPage,
        ).last()) {
            is Result.Success -> {
                Timber.i("Result.Success")
                deviationTrackDao.withTransaction {
                    result.data.map { it.deviationTrack.copy(track = track.toString()) }.run {
                        deviationTrackDao.insertIgnore(this)
                    }
                    result.data.map { it.deviation }.run {
                        deviationDao.upsert(this)
                    }
                }

                MediatorResult.Success(endOfPaginationReached = false)
            }
            is Result.Error -> {
                Timber.i("Result.Success")
                MediatorResult.Error(result.throwable)
            }
            else -> {
                Timber.i("Result else: $result")
                MediatorResult.Success(endOfPaginationReached = true)
            }
        }
    }
}
