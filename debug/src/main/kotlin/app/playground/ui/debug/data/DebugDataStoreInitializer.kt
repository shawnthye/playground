package app.playground.ui.debug.data

import android.app.Application
import app.playground.core.AppInitializer
import coil.Coil
import coil.imageLoader
import coil.util.Logger
import core.playground.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Inject

internal class DebugDataStoreInitializer @Inject constructor(
    private val httpLoggingInterceptor: HttpLoggingInterceptor,
    private val storage: DebugStorage,
    private val coilLogger: Logger,
    @ApplicationScope private val scope: CoroutineScope,
) : AppInitializer {
    override fun init(application: Application) {
        storage.environment.onEach {
            Timber.i("debug environment = $it")
        }.launchIn(scope)

        storage.httpLoggingLevel.onEach {
            Timber.i("httpLoggingInterceptor.level = $it")
            httpLoggingInterceptor.level = it
        }.launchIn(scope)

        storage.coilLogging.onEach {
            val loader = application.imageLoader
            Timber.i("coilLogger.level = $it")
            if (it == CoilLogLevel.NONE) {
                coilLogger.level = CoilLogLevel.NONE.level
                Coil.setImageLoader(loader.newBuilder().logger(null).build())
            } else {
                coilLogger.level = it.level
                Coil.setImageLoader(loader.newBuilder().logger(coilLogger).build())
            }
        }.launchIn(scope)
    }
}
