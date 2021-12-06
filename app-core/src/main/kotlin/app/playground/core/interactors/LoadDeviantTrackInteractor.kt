package app.playground.core.interactors

import app.playground.core.FeatureInteractor
import app.playground.source.of.truth.database.entities.Track
import kotlinx.coroutines.CoroutineDispatcher

abstract class LoadDeviantTrackInteractor(
    coroutineDispatcher: CoroutineDispatcher,
) : FeatureInteractor<Track, Unit>(coroutineDispatcher)
