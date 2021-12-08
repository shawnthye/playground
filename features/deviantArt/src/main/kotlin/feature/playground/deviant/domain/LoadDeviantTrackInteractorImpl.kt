package feature.playground.deviant.domain

import app.playground.core.interactors.LoadDeviantTrackInteractor
import app.playground.store.database.entities.Track
import core.playground.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class LoadDeviantTrackInteractorImpl @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
) : LoadDeviantTrackInteractor(coroutineDispatcher) {
    override suspend fun execute(params: Track) {
        Timber.i("LoadDeviantTrackInteractorImpl#execute")
    }
}
