package api.art.deviant

import api.art.deviant.model.DeviationResult
import core.playground.data.ApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface DeviantArtApi {
    @GET("browse/popular")
    fun popular(
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int = 20,
    ): Flow<ApiResponse<DeviationResult>>
}
