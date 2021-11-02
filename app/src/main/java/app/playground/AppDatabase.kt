package app.playground

import androidx.room.Database
import androidx.room.RoomDatabase
import app.playground.entities.Deviation
import app.playground.entities.PopularDeviation
import feature.playground.deviant.data.DeviationDao

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        Deviation::class,
        PopularDeviation::class,
    ],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviationDao(): DeviationDao
}
