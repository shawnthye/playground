package api.art.deviant

import api.art.deviant.model.Deviation
import api.art.deviant.model.DeviationResult
import core.playground.data.Response
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface DeviantArtApi {

    @GET("browse/{track}")
    fun browse(
        @Path("track") track: String,
        @Query("q") query: String? = "anim",
        @Query("mature_content") mature: Boolean = false,
        @Query("offset") offset: String? = null,
        @Query("limit") limit: Int,
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

    @POST("deviation/sample/action")
    fun postSampleAction(
        @Query("deviationid") id: String,
    ): Flow<Response<Unit>>

    /**
     * for demo purpose
     */
    @GET
    @Headers("Cache-Control: no-cache")
    fun url(
        @Url url: String,
    ): Flow<Response<Unit>>
}
