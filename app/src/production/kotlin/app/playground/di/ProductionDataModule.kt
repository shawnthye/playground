package app.playground.di

import android.app.Application
import coil.ImageLoader
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
object ProductionDataModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = DataModule.createOkHttp()

    @Singleton
    @Provides
    fun providesCoil(
        app: Application,
        okhttp: OkHttpClient,
    ): ImageLoader = DataModule.createImageLoader(app, okhttp)
}
