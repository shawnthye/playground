package feature.playground.product.hunt.posts.domain

import app.playground.store.database.entities.Post
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
) : FlowUseCase<Unit, List<Post>>(coroutineDispatcher) {
    override fun execute(
        params: Unit,
    ): Flow<Result<List<Post>>> = flow {
        emit(Result.Loading())
        emitAll(discoverRepository.queryPosts())
    }
}
