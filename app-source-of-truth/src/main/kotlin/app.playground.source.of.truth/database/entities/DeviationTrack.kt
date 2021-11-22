package app.playground.source.of.truth.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import app.playground.source.of.truth.database.AppEntity
import java.util.Objects

@Entity(
    tableName = "deviation_tracks",
    indices = [
        Index(value = ["track", "deviationId"], unique = true),
    ],
)
data class DeviationTrack(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val track: String,
    val deviationId: String,
    val nextPage: Int,
) : AppEntity

data class TrackWithDeviation(
    @Embedded
    val deviationTrack: DeviationTrack,

    @Relation(parentColumn = "deviationId", entityColumn = "deviationId")
    val deviation: Deviation,
) {

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is TrackWithDeviation -> {
            deviationTrack == other.deviationTrack && deviation == other.deviation
        }
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(deviationTrack, deviation)
}
