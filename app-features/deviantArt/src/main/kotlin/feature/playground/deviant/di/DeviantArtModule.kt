package feature.playground.deviant.di

import api.art.deviant.DeviantArt
import api.art.deviant.DeviantArtApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [DeviantArtDataModule::class])
internal object DeviantArtModule {

    @Singleton
    @Provides
    fun provideDeviantArtApi(deviantArt: DeviantArt): DeviantArtApi {
        return deviantArt.api
    }
}
