package feature.playground.deviant.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import feature.playground.deviant.data.DeviationDataSource
import feature.playground.deviant.data.DeviationDataSourceImpl

@InstallIn(SingletonComponent::class)
@Module
abstract class DeviantArtModule {

    @Binds
    internal abstract fun bindDeviantDataSource(
        source: DeviationDataSourceImpl,
    ): DeviationDataSource
}
