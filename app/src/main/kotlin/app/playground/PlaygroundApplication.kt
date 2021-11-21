package app.playground

import android.app.Application
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder
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
        .componentRegistry {
            val context = this@PlaygroundApplication
            add(SvgDecoder(context))
            add(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoderDecoder(context)
                } else {
                    GifDecoder()
                },
            )
            add(VideoFrameDecoder(context))
        }
        .crossfade(resources.getInteger(android.R.integer.config_shortAnimTime))
        .build()
}
