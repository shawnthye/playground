package feature.playground.deviant.domain

import app.playground.source.of.truth.database.entities.TrackWithDeviation
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import feature.playground.deviant.data.DeviantRepository
import feature.playground.deviant.ui.track.Track
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadTrackDeviantsUseCaseFlow @Inject constructor(
    private val repository: DeviantRepository,
    private val updateTrackUseCase: UpdateTrackUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : FlowUseCase<Track, TrackWithDeviation>(
    dispatcher,
) {
    override fun execute(parameters: Track): Flow<Result<TrackWithDeviation>> {

        TODO("Not yet implemented")
    }
}
