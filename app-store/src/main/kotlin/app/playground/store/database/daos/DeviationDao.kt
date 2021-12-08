package app.playground.store.database.daos

import androidx.room.Dao
import androidx.room.Query
import app.playground.store.database.EntityDao
import app.playground.store.database.entities.Deviation
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DeviationDao : EntityDao<Deviation>() {

    @Query("SELECT deviations.* FROM deviations WHERE deviationId= :id")
    abstract fun observeDeviation(id: String): Flow<Deviation>
}
