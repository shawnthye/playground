package api.art.deviant

import api.art.deviant.model.Deviation
import api.art.deviant.model.DeviationResult
import core.playground.data.Response
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeviantArtApi {
    @GET("browse/popular")
    fun popular(
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int = 20,
    ): Flow<Response<DeviationResult>>

    @Suppress("SpellCheckingInspection")
    @GET("deviation/medadata")
    fun deviationMetadata(
        @Query("deviationids") ids: Array<String>,
    ): Flow<Response<Deviation>>

    @GET("deviation/{deviation-id}")
    fun deviation(
        @Path("deviation-id") id: String,
    ): Flow<Response<Deviation>>

    @Suppress("SpellCheckingInspection")
    @GET("deviation/whofaved")
    fun deviationWhoFaved(
        @Query("deviationid") id: String,
    ): Flow<Response<Deviation>>
}
