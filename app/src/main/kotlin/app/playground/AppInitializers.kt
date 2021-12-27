package app.playground

import android.app.Application
import app.playground.core.AppInitializer
import timber.log.Timber
import javax.inject.Inject

class AppInitializers @Inject constructor(
    private val initializers: Set<@JvmSuppressWildcards AppInitializer>,
) {
    fun init(application: Application) {
        initializers.forEach {
            Timber.i("initializing ${it::class.qualifiedName}")
            it.init(application)
            Timber.i("initialized ${it::class.qualifiedName}")
        }
    }
}
