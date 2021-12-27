package app.playground

import android.app.Application
import app.playground.core.AppInitializer
import coil.Coil
import coil.ImageLoader
import javax.inject.Inject

class CoilInitializer @Inject constructor(
    private val imageLoader: ImageLoader,
) : AppInitializer {

    override fun init(application: Application) {
        Coil.setImageLoader(imageLoader)
    }
}
