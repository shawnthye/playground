package feature.playground.deviant.art.track.domain

import androidx.paging.PagingSource
import app.playground.store.database.entities.Track
import app.playground.store.database.entities.TrackWithDeviation
import core.playground.IoDispatcher
import core.playground.domain.ExperimentalPagingUseCase
import core.playground.domain.PagingUseCase
import feature.playground.deviant.art.track.data.DeviantRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@OptIn(ExperimentalPagingUseCase::class)
class LoadTrackDeviantsUseCase @Inject constructor(
    private val repository: feature.playground.deviant.art.track.data.DeviantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : PagingUseCase<Track, TrackWithDeviation>(dispatcher) {

    override suspend fun shouldRefreshOnLaunch(): Boolean {
        return true
    }

    override fun pagingSource(
        parameters: Track,
    ): PagingSource<Int, TrackWithDeviation> = repository.trackPagingSource(parameters)

    override suspend fun execute(parameters: Track, pageSize: Int, nextPage: String?): Boolean {
        return repository.fetchTrack(parameters, pageSize, nextPage)
    }
}
