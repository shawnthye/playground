package app.playground.di

import app.playground.BuildConfig
import coil.util.DebugLogger
import coil.util.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CoilModule {

    @Singleton
    @Provides
    fun providesCoilLogger(): Logger? {
        return if (!BuildConfig.DEBUG) null else DebugLogger()
    }
}
