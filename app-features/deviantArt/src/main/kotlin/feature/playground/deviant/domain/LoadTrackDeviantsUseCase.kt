// package feature.playground.deviant.domain
//
// import app.playground.source.of.truth.database.entities.TrackWithDeviation
// import core.playground.IoDispatcher
// import core.playground.domain.FlowUseCase
// import core.playground.domain.Result
// import feature.playground.deviant.data.DeviantRepository
// import feature.playground.deviant.ui.track.Track
// import kotlinx.coroutines.CoroutineDispatcher
// import kotlinx.coroutines.flow.Flow
// import javax.inject.Inject
//
// class LoadTrackDeviantsUseCase @Inject constructor(
//     private val repository: DeviantRepository,
//     @IoDispatcher private val dispatcher: CoroutineDispatcher,
// ) : FlowUseCase<Track, List<TrackWithDeviation>>(
//     dispatcher,
// ) {
//
//     override fun execute(
//         parameters: Track,
//     ): Flow<Result<List<TrackWithDeviation>>> = repository.observeTrack(parameters)
// }
