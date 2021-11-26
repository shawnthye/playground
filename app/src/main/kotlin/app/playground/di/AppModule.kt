package app.playground.di

import android.annotation.SuppressLint
import android.os.Build
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
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import timber.log.Timber
import java.util.Date
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providesTimberTree(): Timber.Tree {
        return DebugTree()
    }

    /**
     * This provide the application task, so coroutine will survive until application killed
     */
    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    // HttpLoggingInterceptor { message ->
    //     Timber.tag("Timber:OkHttp").i(message = message)
    // }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
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
    }.build()

    @Singleton
    @Provides
    fun provideJson(): Json = Json {
        prettyPrint = BuildConfig.DEBUG
        ignoreUnknownKeys = true
        // explicitNulls = false
        serializersModule = SerializersModule {
            contextual(Date::class, DateAsLongSerializer)
        }
    }

    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun provideRetrofitConverterFactory(
        json: Json,
    ): Converter.Factory = json.asConverterFactory("application/json".toMediaType())
}

private class DebugTree : Timber.DebugTree() {
    @SuppressLint("ObsoleteSdkInt")
    override fun createStackElementTag(element: StackTraceElement): String {

        val tag = "Timber:${super.createStackElementTag(element)!!}"

        return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= 26) {
            tag
        } else {
            tag.substring(0, MAX_TAG_LENGTH)
        }
    }

    private companion object {
        private const val MAX_TAG_LENGTH = 23
    }
}

object DateAsLongSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "Date", PrimitiveKind.LONG,
    )

    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeLong(value.time)
    override fun deserialize(decoder: Decoder): Date = Date(decoder.decodeLong() * 1000L)
}
