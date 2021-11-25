package app.playground.source.of.truth.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import app.playground.source.of.truth.database.AppEntity
import core.playground.data.Pageable
import core.playground.data.PageableEntry
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
    override val nextPage: String? = null,
) : AppEntity, PageableEntry

data class TrackWithDeviation(

    @Embedded
    override val entry: DeviationTrack,

    @Relation(parentColumn = "deviationId", entityColumn = "deviationId")
    override val relation: Deviation,
) : Pageable<DeviationTrack, Deviation> {

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is TrackWithDeviation -> {
            entry == other.entry && relation == other.relation
        }
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(entry, relation)
}
