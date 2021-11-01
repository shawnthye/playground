package api.art.deviant

import api.art.deviant.model.DeviationResult
import retrofit2.http.GET
import retrofit2.http.Query

interface DeviantArt {
    @GET("browse/popular")
    suspend fun popular(
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int = 10,
    ): DeviationResult
}
