package app.playground.di

import app.playground.ui.AppContainer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
class ProductionUiModule {

    @Provides
    fun providesAppContainer(): AppContainer {
        return AppContainer.DEFAULT
    }
}
