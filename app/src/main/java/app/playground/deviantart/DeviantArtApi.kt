package app.playground.deviantart

import app.playground.deviantart.model.DeviationResult
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

var token: String? = null
val okhttp = OkHttpClient().newBuilder()
    .addNetworkInterceptor(
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        },
    ).build()
val gson = Gson()

data class TokenResult(val access_token: String, val token_type: String, val expires_in: Int)

interface DeviantArtApi {

    @GET("browse/popular")
    suspend fun popular(
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int = 10,
    ): DeviationResult

    companion object {
        fun get(): DeviantArtApi {

            val authenticator = Interceptor { chain ->

                if (token == null) {
                    val form = FormBody.Builder()
                        .add("grant_type", "client_credentials")
                        .add("client_id", "17390")
                        .add(
                            "client_secret",
                            "d88b3cc2995f26b4f5cc76da3166e465",
                        )
                        .build()

                    val tokenRequest = Request.Builder()
                        .url(
                            "https://www.deviantart.com/oauth2/token",
                        )
                        .post(form)
                        .build()

                    val tokenResponse = okhttp.newCall(tokenRequest).execute()

                    GsonConverterFactory.create()
                    tokenResponse.body?.also { body ->
                        val result = gson.fromJson(body.string(), TokenResult::class.java)
                        token = result.access_token
                    }
                }

                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()

                chain.proceed(request)
            }

            return Retrofit.Builder()
                .client(okhttp.newBuilder().addInterceptor(authenticator).build())
                .baseUrl("https://www.deviantart.com/api/v1/oauth2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(DeviantArtApi::class.java)
        }
    }
}
