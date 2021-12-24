package app.playground.di

import app.playground.ui.AppContainer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
internal object ProductionUiModule {

    @Provides
    fun provideAppContainer(): AppContainer {
        return DefaultAppContainer
    }
}

private object DefaultAppContainer : AppContainer
