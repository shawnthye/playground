package app.playground.store.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import app.playground.store.database.AppEntity
import java.util.Date

typealias DeviationId = String

@Entity(
    tableName = "deviations",
    indices = [
        Index(value = ["deviationId"], unique = true),
    ],
)
data class Deviation(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val deviationId: DeviationId,
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
