package app.playground.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tracks_deviations",
    indices = [
        Index(value = ["track", "deviationId"], unique = true),
    ],
)
data class TrackDeviationEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val track: String,
    val deviationId: String,
    val nextPage: Int,
) : AppEntity
