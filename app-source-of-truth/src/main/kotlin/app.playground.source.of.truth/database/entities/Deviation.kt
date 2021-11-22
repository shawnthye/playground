package app.playground.source.of.truth.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.playground.source.of.truth.database.AppEntity

@Entity(tableName = "deviations")
data class Deviation(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val deviationId: String,
    val url: String,
    val title: String,
    val coverUrl: String,
    val imageUrl: String,
    val imageHeight: Int,
    val imageWidth: Int,
) : AppEntity
