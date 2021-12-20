package feature.playground.product.hunt.posts.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import feature.playground.product.hunt.posts.data.DiscoverDataSource
import feature.playground.product.hunt.posts.data.DiscoverDataSourceImpl

@InstallIn(ViewModelComponent::class)
@Module
internal abstract class DiscoveryModuleBinds {

    @Suppress("unused")
    @Binds
    internal abstract fun bindPostDataSource(impl: DiscoverDataSourceImpl): DiscoverDataSource
}
