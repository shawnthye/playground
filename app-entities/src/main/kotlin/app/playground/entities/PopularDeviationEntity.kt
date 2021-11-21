package app.playground.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "popular_deviations",
    indices = [
        Index(value = ["deviationId"], unique = true),
    ],
)
data class PopularDeviationEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val deviationId: String,
) : AppEntity
