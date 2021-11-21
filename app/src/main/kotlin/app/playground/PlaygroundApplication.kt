package app.playground

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class PlaygroundApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var tree: Timber.Tree

    override fun onCreate() {
        super.onCreate()
        Timber.plant(tree)
        Timber.i("timber's tree planted")
    }

    override fun newImageLoader(): ImageLoader = ImageLoader
        .Builder(this)
        .crossfade(resources.getInteger(core.playground.ui.R.integer.rapid_animation))
        .build()
}
