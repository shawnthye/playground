package app.playground

import androidx.room.Database
import androidx.room.RoomDatabase
import app.playground.entities.DeviationEntity
import app.playground.entities.PopularDeviationEntity
import feature.playground.deviant.data.DeviationDao

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        DeviationEntity::class,
        PopularDeviationEntity::class,
    ],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviationDao(): DeviationDao
}
