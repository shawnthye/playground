package feature.playground.deviant.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import feature.playground.deviant.data.DeviantDataSource
import feature.playground.deviant.data.DeviantDataSourceImpl

@InstallIn(SingletonComponent::class)
@Module
abstract class DeviantArtModule {

    @Binds
    internal abstract fun bindDeviantDataSource(source: DeviantDataSourceImpl): DeviantDataSource
}
