package feature.playground.product.hunt.post.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import feature.playground.product.hunt.post.data.PostDataSource
import feature.playground.product.hunt.post.data.PostDataSourceImpl

@InstallIn(ViewModelComponent::class)
@Module
internal abstract class PostModuleBinds {

    @Suppress("unused")
    @Binds
    internal abstract fun bindPostDataSource(impl: PostDataSourceImpl): PostDataSource
}
