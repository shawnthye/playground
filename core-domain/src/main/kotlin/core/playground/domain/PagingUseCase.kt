package core.playground.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import core.playground.data.Pageable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

/**
 * Executes business logic in its execute method and keep posting updates to the result as
 * [Result<R>].
 * Handling an exception (emit [Result.Error] to the result) is the sub-classes responsibility.
 *
 */
abstract class PagingUseCase<in Param, Page>(
    private val coroutineDispatcher: CoroutineDispatcher,
) where Page : Pageable<*, *> {

    protected open val config = PagingConfig(
        pageSize = 10,
        initialLoadSize = 10,
        enablePlaceholders = true,
    )

    operator fun invoke(parameters: Param): Flow<PagingData<Page>> {

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = config,
            remoteMediator = PagedRemoteMediator { pageSize, nextPage ->
                execute(parameters, pageSize, nextPage)
            },
            pagingSourceFactory = { pagingSource(parameters) },
        ).flow.flowOn(coroutineDispatcher)
    }

    protected abstract fun pagingSource(parameters: Param): PagingSource<Int, Page>

    protected abstract suspend fun execute(
        parameters: Param,
        pageSize: Int,
        nextPage: String?,
    ): Boolean
}

@OptIn(ExperimentalPagingApi::class)
private class PagedRemoteMediator<Page>(
    private val doWork: suspend (pageSize: Int, nextPage: String?) -> Boolean,
) : RemoteMediator<Int, Page>() where Page : Pageable<*, *> {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Page>,
    ): MediatorResult {
        val nextPage = when (loadType) {
            LoadType.REFRESH -> {
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                if (state.isEmpty()) {
                    // database paging source is empty
                    return MediatorResult.Success(endOfPaginationReached = false)
                } else {
                    Timber.i("$loadType, nextPage: ${state.lastItemOrNull()?.entry?.nextPage}")
                    val last = state.lastItemOrNull() ?: return MediatorResult.Success(
                        endOfPaginationReached = true,
                    )

                    if (last.entry.nextPage == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        last.entry.nextPage
                    }
                }
            }
        }

        return try {
            val endOfPaginationReached = !doWork(
                when (loadType) {
                    LoadType.REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                },
                nextPage,
            )


            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }
}
