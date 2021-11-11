package app.playground.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "popular_deviations")
data class PopularDeviation(
    @PrimaryKey val id: Long,
    val deviationId: String,
)
