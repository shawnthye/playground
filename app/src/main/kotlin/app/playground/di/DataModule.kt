package app.playground.di

import android.content.Context
import android.os.Build
import app.playground.BuildConfig
import app.playground.data.DateAsLongSerializer
import app.playground.data.ReasonInterceptor
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder
import coil.util.CoilUtils
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import java.util.Date
import javax.inject.Singleton

@DisableInstallInCheck
@Module
object DataModule {

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

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideRetrofitConverterFactory(json: Json): Converter.Factory = json.asConverterFactory(
        "application/json".toMediaType(),
    )

    fun createOkHttp(logging: Interceptor? = null): OkHttpClient {
        val client = OkHttpClient.Builder().apply {
            logging?.also { addNetworkInterceptor(it) }
        }.build()

        return client.newBuilder()
            .addInterceptor(ReasonInterceptor(client))
            .build()
    }

    fun createImageLoader(
        context: Context,
        okhttp: OkHttpClient,
    ): ImageLoader = ImageLoader
        .Builder(context)
        .availableMemoryPercentage(0.75)
        .callFactory { okhttp.newBuilder().cache(CoilUtils.createDefaultCache(context)).build() }
        .componentRegistry {
            add(SvgDecoder(context))
            add(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoderDecoder(context)
                } else {
                    GifDecoder()
                },
            )
            add(VideoFrameDecoder(context))
        }
        .crossfade(context.resources.getInteger(android.R.integer.config_shortAnimTime))
        .build()
}
