package app.playground.ui.debug.domain

import android.content.Context
import app.playground.ui.debug.data.CoilLogLevel
import coil.Coil
import coil.imageLoader
import coil.util.Logger
import core.playground.MainImmediateDispatcher
import core.playground.domain.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SetCoilLogLevelUseCase @Inject constructor(
    private val logger: Logger?,
    @ApplicationContext private val context: Context,
    @MainImmediateDispatcher coroutineDispatcher: CoroutineDispatcher,
) : UseCase<CoilLogLevel, Unit>(
    coroutineDispatcher,
) {
    override suspend fun execute(params: CoilLogLevel) {
        // we are not support to use this on non debug or internal flavor

        if (params == CoilLogLevel.NONE) {
            Coil.setImageLoader(context.imageLoader.newBuilder().logger(null).build())
        } else {
            logger!!.level = params.level
            Coil.setImageLoader(context.imageLoader.newBuilder().logger(logger).build())
        }

        // TODO: 12/13/2021 save loglevel to data store
    }
}
