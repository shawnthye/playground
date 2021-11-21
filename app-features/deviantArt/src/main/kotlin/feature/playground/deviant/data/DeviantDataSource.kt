package feature.playground.deviant.data

import api.art.deviant.DeviantArt
import api.art.deviant.model.Deviation
import api.art.deviant.model.DeviationResult
import app.playground.entities.DeviationEntity
import app.playground.entities.mappers.DeviationResultToEntity
import app.playground.entities.mappers.DeviationToEntity
import core.playground.data.Mapper
import core.playground.data.Response
import core.playground.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

interface DeviantDataSource {
    fun fetchPopular(): Flow<Response<DeviationResult>>

    fun fetchPopular2(): Flow<Result<List<DeviationEntity>>>

    fun fetchDeviation(id: String): Flow<Response<Deviation>>

    fun fetchDeviation2(id: String): Flow<Result<DeviationEntity>>
}

internal class DeviantDataSourceImpl
@Inject constructor(
    private val deviantArt: DeviantArt,
    private val deviationResultToEntity: DeviationResultToEntity,
    private val deviationToEntity: DeviationToEntity,
) : DeviantDataSource {

    private val api by lazy { deviantArt.api }

    override fun fetchPopular(): Flow<Response<DeviationResult>> = api.popular(null)

    override fun fetchPopular2(): Flow<Result<List<DeviationEntity>>> = api.popular(null)
        .toResult(deviationResultToEntity)

    override fun fetchDeviation(id: String): Flow<Response<Deviation>> = api.deviation(id)

    override fun fetchDeviation2(id: String): Flow<Result<DeviationEntity>> = api.deviation(id)
        .toResult(deviationToEntity)
}

private infix fun <
    RequestType,
    ResultType,
    > Flow<Response<RequestType>>.toResult(mapper: Mapper<RequestType, ResultType>): Flow<Result<ResultType>> {
    return transformLatest { response ->
        when (response) {
            is Response.Success -> emit(Result.Success(mapper(response.body)))
            else -> emit(Result.Error(Throwable("Testing"), null))
        }

    }
}
