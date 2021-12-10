package feature.playground.deviant.art.di

import api.art.deviant.DeviantArt
import api.art.deviant.DeviantArtApi
import app.playground.core.navigation.DeviantArtNavActions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import feature.playground.deviant.art.DeviantArtNavActionsImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object DeviantArtModule {

    @Singleton
    @Provides
    fun provideDeviantArtApi(deviantArt: DeviantArt): DeviantArtApi {
        return deviantArt.api
    }

    @Singleton
    @Provides
    fun provideDeviantArtNav(): DeviantArtNavActions = DeviantArtNavActionsImpl()
}
