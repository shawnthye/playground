package feature.playground.deviant.domain

import androidx.paging.PagingSource
import app.playground.source.of.truth.database.entities.TrackWithDeviation
import core.playground.IoDispatcher
import core.playground.domain.ExperimentalPagingUseCase
import core.playground.domain.PagingUseCase
import feature.playground.deviant.data.DeviantRepository
import feature.playground.deviant.ui.track.Track
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@OptIn(ExperimentalPagingUseCase::class)
class LoadTrackDeviantsUseCase @Inject constructor(
    private val repository: DeviantRepository,
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
