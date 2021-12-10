package feature.playground.deviant.art.track.di

import app.playground.core.interactors.LoadDeviantTrackInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import feature.playground.deviant.art.track.data.DeviationDataSource
import feature.playground.deviant.art.track.data.DeviationDataSourceImpl
import feature.playground.deviant.art.track.domain.LoadDeviantTrackInteractorImpl

/**
 * We include this in the main DeviantArtModule manually
 * So we doesn't need to InstallIn
 */
@InstallIn(ViewModelComponent::class)
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
