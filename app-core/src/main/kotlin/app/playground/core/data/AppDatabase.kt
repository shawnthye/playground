package app.playground.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.playground.store.database.daos.DeviationDao
import app.playground.store.database.daos.DeviationTrackDao
import app.playground.store.database.entities.Deviation
import app.playground.store.database.entities.DeviationTrack

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        Deviation::class,
        DeviationTrack::class,
    ],
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviationDao(): DeviationDao
    abstract fun deviationTrackDao(): DeviationTrackDao
}
