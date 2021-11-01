package app.playground

import androidx.room.Database
import androidx.room.RoomDatabase
import app.playground.deviantart.model.Deviation

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        Deviation::class,
    ],
)
abstract class AppDatabase : RoomDatabase() {
}
