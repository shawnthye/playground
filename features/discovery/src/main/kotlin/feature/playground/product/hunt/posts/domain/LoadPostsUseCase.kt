package feature.playground.product.hunt.posts.domain

import api.product.hunt.PostsQuery
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import feature.playground.product.hunt.posts.data.DiscoverRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class LoadPostsUseCase
@Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val discoverRepository: DiscoverRepository,
) : FlowUseCase<Unit, PostsQuery.Data>(coroutineDispatcher) {
    override fun execute(
        params: Unit,
    ): Flow<Result<PostsQuery.Data>> = flow {
        emit(Result.Loading())
        emitAll(discoverRepository.queryPosts())
    }
}
