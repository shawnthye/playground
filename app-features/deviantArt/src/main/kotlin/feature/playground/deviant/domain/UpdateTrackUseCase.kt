package feature.playground.deviant.domain

import app.playground.source.of.truth.database.entities.TrackWithDeviation
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import core.playground.domain.toResult
import feature.playground.deviant.data.DeviantRepository
import feature.playground.deviant.domain.UpdateTrackUseCase.Param
import feature.playground.deviant.ui.track.Track
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateTrackUseCase @Inject constructor(
    private val repository: DeviantRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
) : FlowUseCase<Param, List<TrackWithDeviation>>(
    coroutineDispatcher,
) {
    override fun execute(parameters: Param): Flow<Result<List<TrackWithDeviation>>> =
        repository.fetchTrackAndCache(
            track = parameters.track,
            pageSize = parameters.pageSize,
            nextPage = parameters.page,
        ).toResult()

    data class Param(
        val track: Track,
        val pageSize: Int,
        val page: String?,
    )
}
