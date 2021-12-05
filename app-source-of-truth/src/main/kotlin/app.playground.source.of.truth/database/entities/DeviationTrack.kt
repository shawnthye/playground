package app.playground.source.of.truth.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import app.playground.source.of.truth.database.AppEntity
import core.playground.data.PageableEntry

@Entity(
    tableName = "deviation_tracks",
    indices = [
        Index(value = ["track", "deviationId"], unique = true),
    ],
)
data class DeviationTrack(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val track: Track,
    val deviationId: String,
    override val nextPage: String? = null,
) : AppEntity, PageableEntry

