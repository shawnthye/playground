package app.playground.entities.entries

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import app.playground.entities.Entry
import java.util.Objects

@Entity(
    tableName = "tracks_deviations",
    indices = [
        Index(value = ["track", "deviationId"], unique = true),
    ],
)
data class TrackEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val track: String,
    val deviationId: String,
    val nextPage: Int,
) : Entry

data class TrackWithDeviation(
    @Embedded
    val track: TrackEntity,

    @Relation(parentColumn = "deviationId", entityColumn = "deviationId")
    val deviation: DeviationEntry,
) {

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is TrackWithDeviation -> {
            track == other.track && deviation == other.deviation
        }
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(track, deviation)
}
