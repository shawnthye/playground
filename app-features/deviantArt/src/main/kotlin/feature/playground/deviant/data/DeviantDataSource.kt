package feature.playground.deviant.data

import api.art.deviant.DeviantArt
import api.art.deviant.model.Deviation
import api.art.deviant.model.DeviationResult
import core.playground.data.ApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DeviantDataSource {
    fun fetchPopular(): Flow<ApiResponse<DeviationResult>>

    fun fetchDeviation(id: String): Flow<ApiResponse<Deviation>>
}

internal class DeviantDataSourceImpl
@Inject constructor(
    private val deviantArt: DeviantArt,
) : DeviantDataSource {

    private val api by lazy { deviantArt.api }

    override fun fetchPopular(): Flow<ApiResponse<DeviationResult>> = api.popular(null)

    override fun fetchDeviation(id: String): Flow<ApiResponse<Deviation>> = api.deviation(id)
}
