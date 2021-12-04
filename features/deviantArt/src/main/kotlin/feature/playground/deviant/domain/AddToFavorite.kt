package feature.playground.deviant.domain

import api.art.deviant.DeviantArt
import core.playground.IoDispatcher
import core.playground.data.execute
import core.playground.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher

/**
 * this is just a sample for one shot action
 */
class AddToFavorite(
    private val deviantArt: DeviantArt,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
) : UseCase<String, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameters: String) {
        deviantArt.api.postSampleAction(parameters).execute()
    }
}
