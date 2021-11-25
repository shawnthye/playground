package feature.playground.deviant.domain

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
    private val updateTrackUseCase: UpdateTrackUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : PagingUseCase<Track, TrackWithDeviation>(
    dispatcher,
) {

    override fun doWork(
        parameters: Track,
        pageSize: Int,
        nextPage: String?,
    ): Flow<Result<List<TrackWithDeviation>>> {

        return updateTrackUseCase(
            UpdateTrackUseCase.Param(
                track = parameters,
                pageSize = pageSize,
                page = nextPage,
            ),
        )
    }

    override fun pagingSource(
        parameters: Track,
    ): PagingSource<Int, TrackWithDeviation> = repository.trackPagingSource(parameters)
}
