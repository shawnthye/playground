package app.playground.source.of.truth.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.playground.source.of.truth.database.AppEntity
import java.util.Date

@Entity(tableName = "deviations")
data class Deviation(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val deviationId: String,
    val url: String,
    val title: String,
    val category: String,
    val coverUrl: String,
    val imageUrl: String,
    val imageHeight: Int,
    val imageWidth: Int,
    val authorId: String,
    val authorName: String,
    val authorIconUrl: String,
    val published: Date,
) : AppEntity
