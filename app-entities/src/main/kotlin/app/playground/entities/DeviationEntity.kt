package app.playground.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "deviations",
    indices = [
        Index(value = ["deviationId"], unique = true),
    ],
)
data class DeviationEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val deviationId: String,
    val url: String,
    val title: String,
    val imageSrc: String,
    val imageHeight: Int,
    val imageWidth: Int,
    val track: String?,
) : AppEntity
