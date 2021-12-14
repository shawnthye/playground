package app.playground.di

import app.playground.ui.AppContainer
import app.playground.ui.debug.DebugAppContainer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
internal object InternalUiModule {

    @Provides
    fun providesAppContainer(): AppContainer {
        return DebugAppContainer
    }
}
