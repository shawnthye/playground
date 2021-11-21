package app.playground.core.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.playground.entities.DeviationEntity
import app.playground.entities.TrackDeviationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deviation: DeviationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(
        trackDeviations: List<TrackDeviationEntity>,
        deviations: List<DeviationEntity>,
    )

    @Query("SELECT deviations.* FROM deviations WHERE deviationId = :id")
    fun observeDeviation(id: String): Flow<DeviationEntity>

    @Query(
        """SELECT deviations.* 
        FROM tracks_deviations tracks 
        INNER JOIN deviations ON tracks.deviationId = deviations.deviationId 
        WHERE tracks.track = :track""",
    )
    fun observeTrackDeviations(track: String): Flow<List<DeviationEntity>>

    @Query("SELECT * FROM  deviations")
    fun observeAll(): Flow<List<DeviationEntity>>
}
