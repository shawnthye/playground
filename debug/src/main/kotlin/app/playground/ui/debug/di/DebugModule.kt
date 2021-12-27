package app.playground.ui.debug.di

import app.playground.core.AppInitializer
import app.playground.ui.debug.data.DebugDataStoreInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
internal abstract class DebugModuleBinds {

    @Binds
    @IntoSet
    abstract fun bindDebugDataStoreInitializer(bind: DebugDataStoreInitializer): AppInitializer
}
