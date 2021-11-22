package app.playground.entities.entries

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import app.playground.entities.Entry

@Entity(
    tableName = "deviations",
    indices = [
        Index(value = ["deviationId"], unique = true),
    ],
)
data class DeviationEntry(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val deviationId: String,
    val url: String,
    val title: String,
    val coverUrl: String,
    val imageUrl: String,
    val imageHeight: Int,
    val imageWidth: Int,
) : Entry
