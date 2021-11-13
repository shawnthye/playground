package app.playground

import androidx.room.Database
import androidx.room.RoomDatabase
import app.playground.entities.DeviationEntities
import app.playground.entities.PopularDeviationEntities
import feature.playground.deviant.data.DeviationDao

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        DeviationEntities::class,
        PopularDeviationEntities::class,
    ],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviationDao(): DeviationDao
}
