package feature.playground.deviant.di

import dagger.Binds
import dagger.Module
import dagger.hilt.migration.DisableInstallInCheck
import feature.playground.deviant.data.DeviationDataSource
import feature.playground.deviant.data.DeviationDataSourceImpl

/**
 * We include this in the main DeviantArtModule manually
 * So we doesn't need to InstallIn
 */
@DisableInstallInCheck
@Module
abstract class DeviantArtDataModule {

    @Suppress("unused")
    @Binds
    internal abstract fun bindDeviantDataSource(impl: DeviationDataSourceImpl): DeviationDataSource
}
