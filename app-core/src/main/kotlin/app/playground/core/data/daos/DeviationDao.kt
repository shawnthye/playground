package app.playground.core.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.playground.entities.EntityDao
import app.playground.entities.entities.Deviation
import app.playground.entities.entities.DeviationTrack
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DeviationDao : EntityDao<Deviation>() {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertTracks(
        deviationTracks: List<DeviationTrack>,
        deviations: List<Deviation>,
    )

    @Query("SELECT deviations.* FROM deviations WHERE deviationId= :id")
    abstract fun observeDeviation(id: String): Flow<Deviation>
}
