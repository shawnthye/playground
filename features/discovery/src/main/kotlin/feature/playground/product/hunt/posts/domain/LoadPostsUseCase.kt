package feature.playground.product.hunt.posts.domain

import app.playground.store.database.entities.Discovery
import core.playground.IoDispatcher
import core.playground.domain.ExperimentalPagingUseCase
import core.playground.domain.PagingUseCase
import feature.playground.product.hunt.posts.data.DiscoverRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@OptIn(ExperimentalPagingUseCase::class)
internal class LoadPostsUseCase
@Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val discoverRepository: DiscoverRepository,
) : PagingUseCase<Unit, Discovery>(coroutineDispatcher) {

    override fun pagingSource(parameters: Unit) = discoverRepository.pagingSource()

    override suspend fun execute(parameters: Unit, pageSize: Int, nextPage: String?): Boolean {
        return discoverRepository.queryNext(nextPage)
    }
}
