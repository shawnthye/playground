package app.playground.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import app.playground.core.data.daos.DeviationDao
import app.playground.entities.entries.DeviationEntry
import app.playground.entities.entries.TrackEntity

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        DeviationEntry::class,
        TrackEntity::class,
    ],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviationDao(): DeviationDao
}
