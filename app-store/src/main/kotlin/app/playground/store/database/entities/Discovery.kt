package app.playground.store.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import core.playground.data.Pageable
import java.util.Objects

data class Discovery(
    @Embedded
    override val entry: DiscoveryPostEntry,

    @Relation(parentColumn = "postId", entityColumn = "postId")
    val post: Post,
) : Pageable<DiscoveryPostEntry> {

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is Discovery -> {
            entry.postId == other.entry.postId && post == other.post
        }
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(entry, post)
}
