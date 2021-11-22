package feature.playground.deviant.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import api.art.deviant.DeviantArt
import app.playground.entities.entries.DeviationEntry
import app.playground.entities.entries.TrackEntity
import app.playground.entities.mappers.DeviationToEntity
import app.playground.entities.mappers.TrackDeviationsToEntity
import core.playground.domain.Result
import core.playground.domain.toResult
import feature.playground.deviant.ui.track.Track
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DeviationDataSource {
    fun browseDeviations(track: Track): Flow<Result<Pair<List<TrackEntity>, List<DeviationEntry>>>>
    fun getDeviation(id: String): Flow<Result<DeviationEntry>>
}

internal class DeviationDataSourceImpl @Inject constructor(
    private val deviantArt: DeviantArt,
    private val trackDeviationsToEntity: TrackDeviationsToEntity,
    private val deviationToEntity: DeviationToEntity,
) : DeviationDataSource {

    private val api by lazy { deviantArt.api }

    override fun browseDeviations(
        track: Track,
    ): Flow<Result<Pair<List<TrackEntity>, List<DeviationEntry>>>> {
        return api.browse(track = track.toString().lowercase()).toResult(trackDeviationsToEntity)
    }

    override fun getDeviation(id: String): Flow<Result<DeviationEntry>> = api.deviation(id)
        .toResult(deviationToEntity)

    suspend fun aaa(): ABC {
        return ABC()
    }
}

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
