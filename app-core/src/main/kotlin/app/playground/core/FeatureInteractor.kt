package app.playground.core

import core.playground.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher

abstract class FeatureInteractor<in P, R>(
    coroutineDispatcher: CoroutineDispatcher,
) : UseCase<P, R>(coroutineDispatcher)
