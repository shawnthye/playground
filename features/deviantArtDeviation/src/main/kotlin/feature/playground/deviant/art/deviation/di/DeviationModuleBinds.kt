package feature.playground.deviant.art.deviation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import feature.playground.deviant.art.deviation.data.DeviationDataSource
import feature.playground.deviant.art.deviation.data.RemoteDeviationDataSource

@InstallIn(FragmentComponent::class)
@Module
internal abstract class DeviationModuleBinds {

    @Suppress("unused")
    @Binds
    abstract fun bindDeviationDataSource(
        remoteDeviationDataSource: RemoteDeviationDataSource,
    ): DeviationDataSource
}
