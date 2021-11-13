package app.playground.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deviations")
data class DeviationEntities(
    @PrimaryKey val id: String,
    val url: String,
    val title: String,
    val imageSrc: String,
    val imageHeight: Int,
    val imageWidth: Int,

    val track: String?,
)
