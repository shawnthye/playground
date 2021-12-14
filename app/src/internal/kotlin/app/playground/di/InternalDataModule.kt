package app.playground.di

import android.app.Application
import app.playground.ui.debug.data.DebugEnvironment
import app.playground.ui.debug.data.DebugStorage
import coil.ImageLoader
import coil.util.DebugLogger
import coil.util.Logger
import core.playground.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(
    includes = [DataModule::class],
)
object InternalDataModule {

    @Singleton
    @Provides
    fun providesDebugEnvironment(
        @ApplicationScope scope: CoroutineScope,
        debugStorage: DebugStorage,
    ): DebugEnvironment = runBlocking(scope.coroutineContext) {
        debugStorage.environment.first()
    }

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return DataModule.createOkHttp(logging = loggingInterceptor)
    }

    @Singleton
    @Provides
    fun providesCoilLogger(): Logger = DebugLogger()

    @Singleton
    @Provides
    fun providesCoil(
        app: Application,
        okhttp: OkHttpClient,
        logger: Logger,
    ): ImageLoader = DataModule.createImageLoader(app, okhttp).newBuilder()
        .logger(logger = logger)
        .build()
}
