package feature.playground.product.hunt.post.domain

import api.product.hunt.PostQuery
import core.playground.IoDispatcher
import core.playground.domain.FlowUseCase
import core.playground.domain.Result
import feature.playground.product.hunt.post.data.PostRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

typealias POST_ID = String

/**
 * This is very first of the product hunt graphQL api, still experimental
 */
internal class LoadPostUseCase
@Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: PostRepository,
) : FlowUseCase<POST_ID, PostQuery.Data>(coroutineDispatcher) {
    override fun execute(params: POST_ID): Flow<Result<PostQuery.Data>> = flow {
        emit(Result.Loading())

        val query = repository.queryPost(params)

        if (query.errors.isNullOrEmpty().not()) {
            emit(Result.Error(Exception(query.errors!!.joinToString("\n") { it.message })))
        }

        emit(Result.Success(query.data!!))
    }
}
