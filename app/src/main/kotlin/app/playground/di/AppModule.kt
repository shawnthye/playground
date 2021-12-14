package app.playground.di

import android.annotation.SuppressLint
import android.os.Build
import app.playground.BuildConfig
import app.playground.data.DateAsLongSerializer
import app.playground.data.ReasonInterceptor
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
