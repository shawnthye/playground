package app.playground.deviantart

import app.playground.deviantart.model.DeviationResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DeviantArtApi {

    @Headers("Authorization: Bearer ddd0a073ffeba34116424950451eaebb48658fbd6432681b57")
    @GET("browse/popular")
    suspend fun popular(
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int = 10,
    ): DeviationResult

    companion object {
        fun get(): DeviantArtApi {
            return Retrofit.Builder()
                .baseUrl("https://www.deviantart.com/api/v1/oauth2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DeviantArtApi::class.java)
        }
    }
}
