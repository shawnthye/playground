package app.playground.di

import app.playground.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import core.playground.ApplicationScope
import core.playground.DefaultDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import timber.log.Timber
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providesTimberTree(): Timber.Tree = Timber.DebugTree()

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
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder().apply {
        // dns(
        //     object : Dns {
        //         override fun lookup(hostname: String): List<InetAddress> {
        //             val publicDNS = listOf(
        //                 // Google
        //                 InetAddress.getByName("8.8.8.8"),
        //                 InetAddress.getByName("8.8.4.4"),
        //
        //                 // // Quad9
        //                 // InetAddress.getByName("9.9.9.9"),
        //                 // InetAddress.getByName("149.112.112.112"),
        //                 //
        //                 // // OpenDns
        //                 // InetAddress.getByName("208.67.222.222"),
        //                 // InetAddress.getByName("208.67.220.220"),
        //             )
        //
        //             val defaultDns = InetAddress.getAllByName(hostname).toList()
        //
        //             return defaultDns + publicDNS
        //         }
        //     },
        // )
        addNetworkInterceptor(loggingInterceptor)
        addNetworkInterceptor(ErrorInterceptor)
    }.build()

    @Singleton
    @Provides
    fun provideJson(): Json = Json {
        prettyPrint = BuildConfig.DEBUG
        ignoreUnknownKeys = true
    }

    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun provideRetrofitConverterFactory(
        json: Json,
    ): Converter.Factory = json.asConverterFactory("application/json".toMediaType())
}

object ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        return response
    }
}
