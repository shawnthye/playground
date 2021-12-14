package app.playground.di

import android.app.Application
import coil.ImageLoader
import coil.util.DebugLogger
import coil.util.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
