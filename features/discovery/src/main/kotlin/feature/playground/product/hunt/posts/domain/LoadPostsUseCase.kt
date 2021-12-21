package feature.playground.product.hunt.posts.domain

import androidx.paging.PagingConfig
import app.playground.store.database.entities.Discovery
import core.playground.IoDispatcher
import core.playground.domain.PagingUseCase
import feature.playground.product.hunt.posts.data.DiscoverRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class LoadPostsUseCase
@Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val discoverRepository: DiscoverRepository,
) : PagingUseCase<Unit, Discovery>(coroutineDispatcher) {

    override fun pageConfig(): PagingConfig {

        val pageSize = super.pageConfig().pageSize

        return PagingConfig(
            pageSize = pageSize,
            initialLoadSize = pageSize,
        )
    }

    override fun pagingSource(parameters: Unit) = discoverRepository.pagingSource()

    override suspend fun execute(parameters: Unit, pageSize: Int, nextPage: String?): Boolean {
        return discoverRepository.queryNext(nextPage)
    }
}
