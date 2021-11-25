package core.playground.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.RemoteMediator.MediatorResult
import core.playground.data.Pageable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
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

    operator fun invoke(parameters: Param): Flow<PagingData<Page>> {

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = config,
            remoteMediator = PagedRemoteMediator { pageSize, nextPage ->
                when (val result = doWork(parameters, pageSize, nextPage).last()) {
                    is Result.Success -> {
                        if (result.data.isEmpty()) {
                            MediatorResult.Success(endOfPaginationReached = true)
                        } else {
                            MediatorResult.Success(endOfPaginationReached = false)
                        }
                    }
                    is Result.Error -> {
                        MediatorResult.Error(result.throwable)
                    }
                    is Result.Loading -> MediatorResult.Success(endOfPaginationReached = true)
                }
            },
            pagingSourceFactory = { pagingSource(parameters) },
        ).flow
    }

    protected abstract val config: PagingConfig

    protected abstract fun doWork(
        parameters: Param,
        pageSize: Int,
        nextPage: String?,
    ): Flow<Result<List<Page>>>

    protected abstract fun pagingSource(parameters: Param): PagingSource<Int, Page>
}

@OptIn(ExperimentalPagingApi::class)
private class PagedRemoteMediator<Page>(
    private val doWork: suspend (pageSize: Int, nextPage: String?) -> MediatorResult,
) : RemoteMediator<Int, Page>() where Page : Pageable<*, *> {

    // override suspend fun initialize(): InitializeAction {
    //     return InitializeAction.LAUNCH_INITIAL_REFRESH
    // }

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
                    // source is empty
                    null
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

        return doWork(
            when (loadType) {
                LoadType.REFRESH -> state.config.initialLoadSize
                else -> state.config.pageSize
            },
            nextPage,
        )
    }
}
