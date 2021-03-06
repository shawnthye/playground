package core.playground.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

/**
 * Executes business logic in its execute method and keep posting updates to the result as
 * [Result<R>].
 * Handling an exception (emit [Result.Error] to the result) is the sub-classes responsibility.
 *
 */
abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {
    operator fun invoke(params: P): Flow<Result<R>> = execute(params)
        .flowOn(coroutineDispatcher)
        .catch { throwable ->
            emit(
                throwable.catchOrThrow {
                    Timber.tag("FlowUseCase").e(throwable)
                    Result.Error(throwable, null)
                },
            )
        }

    protected abstract fun execute(params: P): Flow<Result<R>>
}
