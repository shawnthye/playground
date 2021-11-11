package api.art.deviant

import core.playground.data.FlowCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviantArt @Inject constructor(
    private val client: OkHttpClient,
    private val authenticator: AuthenticatorInterceptor,
    private val converter: GsonConverterFactory,
) {

    val api: DeviantArtApi by lazy {

        val artClient = client.newBuilder()
            .addInterceptor(authenticator)
            .build()

        Retrofit.Builder()
            .baseUrl("https://www.deviantart.com/api/v1/oauth2/")
            .client(artClient)
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .addConverterFactory(converter)
            .build()
            .create(DeviantArtApi::class.java)
    }
}
