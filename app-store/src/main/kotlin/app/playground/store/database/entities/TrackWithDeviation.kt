package app.playground.store.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import core.playground.data.Pageable
import java.util.Objects

data class TrackWithDeviation(
    @Embedded
    override val entry: DeviationTrack,

    @Relation(parentColumn = "deviationId", entityColumn = "deviationId")
    val deviation: Deviation,
) : Pageable<DeviationTrack> {

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is TrackWithDeviation -> {
            entry == other.entry && deviation == other.deviation
        }
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(entry, deviation)
}
