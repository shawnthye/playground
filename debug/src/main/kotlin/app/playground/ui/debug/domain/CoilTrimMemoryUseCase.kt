package app.playground.ui.debug.domain

import android.content.ComponentCallbacks2
import android.content.Context
import coil.imageLoader
import core.playground.IoDispatcher
import core.playground.domain.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class CoilTrimMemoryUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, Unit>(dispatcher) {
    override suspend fun execute(params: Unit) {
        context.imageLoader.memoryCache.clear()
        context.imageLoader.bitmapPool.trimMemory(ComponentCallbacks2.TRIM_MEMORY_COMPLETE)
        context.imageLoader.bitmapPool.clear()
        // CoilUtils.createDefaultCache(context).directory.deleteRecursively()
        Runtime.getRuntime().gc()
    }
}
