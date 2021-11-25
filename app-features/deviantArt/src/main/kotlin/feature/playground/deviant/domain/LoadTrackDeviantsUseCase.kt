package feature.playground.deviant.domain

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import app.playground.source.of.truth.database.entities.TrackWithDeviation
import core.playground.IoDispatcher
import core.playground.domain.PagingUseCase
import core.playground.domain.Result
import feature.playground.deviant.data.DeviantRepository
import feature.playground.deviant.ui.track.Track
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadTrackDeviantsUseCase @Inject constructor(
    private val repository: DeviantRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : PagingUseCase<Track, TrackWithDeviation>(
    dispatcher,
) {
    override val config: PagingConfig
        get() = PagingConfig(pageSize = 10, initialLoadSize = 10)

    override fun doWork(
        parameters: Track,
        pageSize: Int,
        nextPage: String?,
    ): Flow<Result<List<TrackWithDeviation>>> {
        return repository.aaaaa(track = parameters, pageSize = pageSize, nextPage = nextPage)
    }

    override fun pagingSource(parameters: Track): PagingSource<Int, TrackWithDeviation> {
        return repository.paging(parameters)
    }
}
