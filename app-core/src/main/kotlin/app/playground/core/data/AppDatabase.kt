package app.playground.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.playground.store.database.daos.DeviationDao
import app.playground.store.database.daos.DeviationTrackDao
import app.playground.store.database.daos.PostDao
import app.playground.store.database.entities.Deviation
import app.playground.store.database.entities.DeviationTrack
import app.playground.store.database.entities.Post

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        Post::class,
        Deviation::class,
        DeviationTrack::class,
    ],
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun deviationDao(): DeviationDao
    abstract fun deviationTrackDao(): DeviationTrackDao
}
