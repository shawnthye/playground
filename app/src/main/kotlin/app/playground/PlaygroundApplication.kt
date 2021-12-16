package app.playground

import android.app.Application
import app.playground.utils.CrashlyticsTree
import app.playground.utils.DebugTree
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class PlaygroundApplication : Application(), ImageLoaderFactory {

    init {
        // we need Timber as soon as possible
        if (!BuildConfig.DEBUG) {
            Timber.plant(CrashlyticsTree)
        } else {
            Timber.plant(DebugTree())
        }
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate() {
        Timber.i("onCreate()")
        super.onCreate()
    }

    override fun newImageLoader(): ImageLoader = imageLoader
}
