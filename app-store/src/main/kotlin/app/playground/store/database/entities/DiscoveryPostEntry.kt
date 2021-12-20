package app.playground.store.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import app.playground.store.database.AppEntity
import core.playground.data.PageableEntry

@Entity(
    tableName = "discovery",
    indices = [
        Index(value = ["postId"], unique = true),
    ],
)
data class DiscoveryPostEntry(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    override val nextPage: String?,
    val postId: PostId,
) : AppEntity, PageableEntry
