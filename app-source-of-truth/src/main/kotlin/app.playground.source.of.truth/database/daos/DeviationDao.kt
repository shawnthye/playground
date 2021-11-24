package app.playground.source.of.truth.database.daos

import androidx.room.Dao
import androidx.room.Query
import app.playground.source.of.truth.database.EntityDao
import app.playground.source.of.truth.database.entities.Deviation
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DeviationDao : EntityDao<Deviation>() {

    @Query("SELECT deviations.* FROM deviations WHERE deviationId= :id")
    abstract fun observeDeviation(id: String): Flow<Deviation>
}
