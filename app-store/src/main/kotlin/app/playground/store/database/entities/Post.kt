package app.playground.store.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import app.playground.store.database.AppEntity

typealias PostId = String

@Entity(
    tableName = "posts",
    indices = [
        Index(value = ["postId"], unique = true),
    ],
)
data class Post(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val postId: PostId,
    val name: String,
    val thumbnailUrl: String?,
) : AppEntity {
    override fun equals(other: Any?): Boolean {
        return (other as? Post)?.postId == postId
    }

    override fun hashCode(): Int {
        return postId.hashCode()
    }
}
