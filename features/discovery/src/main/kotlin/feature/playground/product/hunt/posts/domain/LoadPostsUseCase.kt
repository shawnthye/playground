package feature.playground.product.hunt.posts.domain

import api.product.hunt.ExperimentalProductHuntApi
import api.product.hunt.PostsQuery
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import feature.playground.product.hunt.posts.data.DiscoverRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This is very first of the product hunt graphQL api, still experimental
 */
@OptIn(ExperimentalProductHuntApi::class)
internal class LoadPostsUseCase
@Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val discoverRepository: DiscoverRepository,
) : FlowUseCase<Unit, PostsQuery.Data>(coroutineDispatcher) {
    override fun execute(params: Unit): Flow<Result<PostsQuery.Data>> = flow {
        emit(Result.Loading())

        val query = discoverRepository.queryPosts()

        if (query.errors.isNullOrEmpty().not()) {
            emit(Result.Error(Exception(query.errors!!.joinToString("\n") { it.message })))
        }

        emit(Result.Success(query.data!!))
    }
}
