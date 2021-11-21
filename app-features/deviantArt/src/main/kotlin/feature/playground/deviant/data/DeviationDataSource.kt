package feature.playground.deviant.data

import api.art.deviant.DeviantArt
import app.playground.entities.DeviationEntity
import app.playground.entities.PopularDeviationEntity
import app.playground.entities.mappers.DeviationToEntity
import app.playground.entities.mappers.PopularResultToEntity
import core.playground.domain.Result
import core.playground.domain.toEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DeviationDataSource {
    fun fetchPopular(): Flow<Result<Pair<List<PopularDeviationEntity>, List<DeviationEntity>>>>
    fun fetchDeviation(id: String): Flow<Result<DeviationEntity>>
}

internal class DeviationDataSourceImpl @Inject constructor(
    private val deviantArt: DeviantArt,
    private val popularResultToEntity: PopularResultToEntity,
    private val deviationToEntity: DeviationToEntity,
) : DeviationDataSource {

    private val api by lazy { deviantArt.api }

    override fun fetchPopular(): Flow<
        Result<Pair<List<PopularDeviationEntity>, List<DeviationEntity>>>,
        > = api.popular(null).toEntity(popularResultToEntity)

    override fun fetchDeviation(id: String): Flow<Result<DeviationEntity>> = api.deviation(id)
        .toEntity(deviationToEntity)
}
