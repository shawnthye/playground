package app.playground.di

import android.app.Application
import coil.ImageLoader
import core.playground.data.CallFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
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
    fun provideOkHttpClient(): OkHttpClient = DataModule.createOkHttp()

    @Singleton
    @Provides
    fun provideCallFactory(): CallFactory = object : CallFactory() {
        override fun invoke(block: () -> OkHttpClient): Call.Factory = block()
    }

    @Singleton
    @Provides
    fun provideCoil(app: Application): ImageLoader = DataModule.createImageLoader(app)
}
