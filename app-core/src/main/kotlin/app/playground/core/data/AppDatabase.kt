package app.playground.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import app.playground.core.data.daos.DeviationDao
import app.playground.entities.DeviationEntity
import app.playground.entities.PopularDeviationEntity

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