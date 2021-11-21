package feature.playground.deviant.data

import api.art.deviant.DeviantArt
import app.playground.entities.DeviationEntity
import app.playground.entities.PopularDeviationEntity
import app.playground.entities.mappers.DeviationToEntity
import app.playground.entities.mappers.PopularDeviationsToEntity
import core.playground.domain.Result
import core.playground.domain.toResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DeviationDataSource {
    fun getPopularDeviations(): Flow<Result<Pair<List<PopularDeviationEntity>, List<DeviationEntity>>>>
    fun getDeviation(id: String): Flow<Result<DeviationEntity>>
}

internal class DeviationDataSourceImpl @Inject constructor(
    private val deviantArt: DeviantArt,
    private val popularDeviationsToEntity: PopularDeviationsToEntity,
    private val deviationToEntity: DeviationToEntity,
) : DeviationDataSource {

    private val api by lazy { deviantArt.api }

    override fun getPopularDeviations(): Flow<
        Result<
            Pair<
                List<PopularDeviationEntity>,
                List<DeviationEntity>,
                >,
            >,
        > = api.popular(null).toResult(popularDeviationsToEntity)

    override fun getDeviation(id: String): Flow<Result<DeviationEntity>> = api.deviation(id)
        .toResult(deviationToEntity)
}
