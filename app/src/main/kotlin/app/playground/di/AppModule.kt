package app.playground.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import core.playground.ApplicationScope
import core.playground.DefaultDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    /**
     * This provide the application task, so coroutine will survive until application killed
     */
    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder().apply {
        dns(
            object : Dns {
                override fun lookup(hostname: String): List<InetAddress> {
                    val publicDNS = listOf(
                        // Google
                        InetAddress.getByName("2001:4860:4860::8888"),
                        InetAddress.getByName("2001:4860:4860::8844"),
                        InetAddress.getByName("8.8.8.8"),
                        InetAddress.getByName("8.8.4.4"),

                        // Quad9
                        InetAddress.getByName("9.9.9.9"),
                        InetAddress.getByName("149.112.112.112"),
                        InetAddress.getByName("2620:fe::fe"),
                        InetAddress.getByName("2620:fe::9"),

                        // OpenDns
                        InetAddress.getByName("208.67.222.222"),
                        InetAddress.getByName("208.67.220.220"),
                    )

                    val defaultDns = InetAddress.getAllByName(hostname).toList()

                    return defaultDns + publicDNS
                }
            },
        )

        addNetworkInterceptor(loggingInterceptor)
    }.build()

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideGsonConverterFactory(
        gson: Gson,
    ): GsonConverterFactory = GsonConverterFactory.create(gson)
}