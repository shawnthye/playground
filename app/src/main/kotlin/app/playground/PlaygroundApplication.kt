package app.playground

import android.app.Application
import app.playground.utils.CrashlyticsTree
import app.playground.utils.DebugTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class PlaygroundApplication : Application() {

    init {
        // we need Timber as soon as possible
        if (!BuildConfig.DEBUG) {
            Timber.plant(CrashlyticsTree)
        } else {
            Timber.plant(DebugTree())
        }
    }

    @Inject
    lateinit var initializers: AppInitializers

    override fun onCreate() {
        Timber.i("before onCreate()")
        super.onCreate()
        initializers.init(this)
    }
}
