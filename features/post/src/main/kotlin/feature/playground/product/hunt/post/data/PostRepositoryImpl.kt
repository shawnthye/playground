package feature.playground.product.hunt.post.data

import app.playground.store.database.daos.PostDao
import app.playground.store.database.entities.Post
import app.playground.store.database.entities.PostId
import core.playground.domain.Result
import core.playground.domain.asNetworkBoundResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal interface PostRepository {
    fun observePost(id: PostId): Flow<Result<Post>>
}

internal class PostRepositoryImpl
@Inject constructor(
    private val postDao: PostDao,
    private val postDataSource: PostDataSource,
) : PostRepository {
    override fun observePost(id: PostId): Flow<Result<Post>> {
        return postDataSource.queryPost(id).asNetworkBoundResult(
            query = postDao.observePost(id),
        ) {
            postDao.upsert(it)
        }
    }
}
