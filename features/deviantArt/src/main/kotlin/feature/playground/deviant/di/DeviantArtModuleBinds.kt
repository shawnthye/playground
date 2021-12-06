package feature.playground.deviant.di

import app.playground.core.interactors.LoadDeviantTrackInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.migration.DisableInstallInCheck
import feature.playground.deviant.data.DeviationDataSource
import feature.playground.deviant.data.DeviationDataSourceImpl
import feature.playground.deviant.domain.LoadDeviantTrackInteractorImpl

/**
 * We include this in the main DeviantArtModule manually
 * So we doesn't need to InstallIn
 */
@DisableInstallInCheck
@Module
internal abstract class DeviantArtModuleBinds {

    @Suppress("unused")
    @Binds
    internal abstract fun bindDeviantDataSource(impl: DeviationDataSourceImpl): DeviationDataSource

    @Suppress("unused")
    @Binds
    internal abstract fun bindLoadDeviantTrackInteractor(
        impl: LoadDeviantTrackInteractorImpl,
    ): LoadDeviantTrackInteractor
}
