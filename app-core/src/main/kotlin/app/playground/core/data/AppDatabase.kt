package app.playground.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import app.playground.core.data.daos.DeviationDao
import app.playground.core.data.daos.DeviationTrackDao
import app.playground.entities.entities.Deviation
import app.playground.entities.entities.DeviationTrack

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        Deviation::class,
        DeviationTrack::class,
    ],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviationDao(): DeviationDao
    abstract fun deviationTrackDao(): DeviationTrackDao
}
