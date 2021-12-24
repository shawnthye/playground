package app.playground.di

import android.app.Application
import app.playground.ui.debug.data.DebugEnvironment
import app.playground.ui.debug.data.DebugStorage
import app.playground.ui.debug.data.HttpEngine
import coil.ImageLoader
import coil.util.DebugLogger
import coil.util.Logger
import core.playground.ApplicationScope
import core.playground.data.CallFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(
    includes = [DataModule::class],
)
object InternalDataModule {

    @Singleton
    @Provides
    fun provideDebugEnvironment(
        @ApplicationScope scope: CoroutineScope,
        debugStorage: DebugStorage,
    ): DebugEnvironment = runBlocking(scope.coroutineContext) {
        debugStorage.environment.first()
    }

    @Singleton
    @Provides
    fun provideHttpEngine(
        @ApplicationScope scope: CoroutineScope,
        debugStorage: DebugStorage,
    ): HttpEngine = runBlocking(scope.coroutineContext) {
        debugStorage.networkHttpEngine.first()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return DataModule.createOkHttp(logging = loggingInterceptor)
    }

    @Singleton
    @Provides
    fun provideCallFactory(httpEngine: HttpEngine): CallFactory = object : CallFactory() {
        override fun invoke(okhttp: OkHttpClient): Call.Factory {
            return when (httpEngine) {
                HttpEngine.OKHTTP -> okhttp
                HttpEngine.CRONET -> CronetFactory(okhttp)
            }
        }
    }

    @Singleton
    @Provides
    fun provideCoilLogger(): Logger = DebugLogger()

    @Singleton
    @Provides
    fun provideCoil(
        app: Application,
        logger: Logger,
    ): ImageLoader = DataModule.createImageLoader(app).newBuilder()
        .logger(logger = logger)
        .build()
}

class CronetFactory(private val okhttp: OkHttpClient) : Call.Factory {
    override fun newCall(request: Request): Call {
        return okhttp.newCall(request)
    }
}
