package feature.playground.deviant.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import api.art.deviant.DeviantArt
import core.playground.domain.Result
import core.playground.domain.toResult
import feature.playground.deviant.ui.track.Track
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DeviationDataSource {
    fun browseDeviations(track: Track): Flow<Result<Pair<List<app.playground.source.of.truth.database.entities.DeviationTrack>, List<app.playground.source.of.truth.database.entities.Deviation>>>>
    fun getDeviation(id: String): Flow<Result<app.playground.source.of.truth.database.entities.Deviation>>
}

internal class DeviationDataSourceImpl @Inject constructor(
    private val deviantArt: DeviantArt,
    private val trackDeviationsToEntity: app.playground.source.of.truth.mappers.TrackDeviationsToEntity,
    private val deviationToEntity: app.playground.source.of.truth.mappers.DeviationToEntity,
) : DeviationDataSource {

    private val api by lazy { deviantArt.api }

    override fun browseDeviations(
        track: Track,
    ): Flow<Result<Pair<List<app.playground.source.of.truth.database.entities.DeviationTrack>, List<app.playground.source.of.truth.database.entities.Deviation>>>> {
        return api.browse(track = track.toString().lowercase()).toResult(trackDeviationsToEntity)
    }

    override fun getDeviation(id: String): Flow<Result<app.playground.source.of.truth.database.entities.Deviation>> =
        api.deviation(id)
            .toResult(deviationToEntity)
}

@Suppress("unused")
@OptIn(ExperimentalPagingApi::class)
class ABC : RemoteMediator<String, String>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<String, String>,
    ): MediatorResult {
        return MediatorResult.Error(throwable = Throwable(""))
    }

    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }
}
