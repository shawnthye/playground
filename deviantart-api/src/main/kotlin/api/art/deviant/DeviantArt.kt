package api.art.deviant

import core.playground.data.CallFactory
import core.playground.data.FlowCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviantArt @Inject constructor(
    private val client: OkHttpClient,
    private val callFactory: CallFactory,
    private val authenticator: DeviantArtAuthenticator,
    private val converterFactory: Converter.Factory,
) {

    val api: DeviantArtApi by lazy {

        val artClient = client.newBuilder()
            .authenticator(authenticator)
            .addInterceptor(authenticator.interceptor)
            .build()

        Retrofit.Builder()
            .baseUrl("https://www.deviantart.com/api/v1/oauth2/")
            .callFactory(callFactory { artClient })
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .addConverterFactory(converterFactory)
            .build()
            .create(DeviantArtApi::class.java)
    }
}
