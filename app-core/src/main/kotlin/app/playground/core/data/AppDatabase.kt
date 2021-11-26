package app.playground.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.playground.source.of.truth.database.daos.DeviationDao
import app.playground.source.of.truth.database.daos.DeviationTrackDao
import app.playground.source.of.truth.database.entities.Deviation
import app.playground.source.of.truth.database.entities.DeviationTrack

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
