package app.playground.store.database.daos

import androidx.room.Dao
import androidx.room.Query
import app.playground.store.database.EntityDao
import app.playground.store.database.entities.Post
import app.playground.store.database.entities.PostId
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PostDao : EntityDao<Post>() {

    @Query("SELECT * FROM posts WHERE postId = :postId")
    abstract fun observePost(postId: PostId): Flow<Post>
}
