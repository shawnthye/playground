package feature.playground.product.hunt.post.domain

import app.playground.store.database.entities.Post
import app.playground.store.database.entities.PostId
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import feature.playground.product.hunt.post.data.PostRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LoadPostUseCase
@Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: PostRepository,
) : FlowUseCase<PostId, Post>(coroutineDispatcher) {

    override fun execute(
        params: PostId,
    ): Flow<Result<Post>> = repository.observePost(params)
}
