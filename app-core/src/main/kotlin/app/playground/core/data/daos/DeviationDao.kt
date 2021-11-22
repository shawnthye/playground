package app.playground.core.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import app.playground.entities.entries.DeviationEntry
import app.playground.entities.entries.TrackEntity
import app.playground.entities.entries.TrackWithDeviation
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deviation: DeviationEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(
        tracks: List<TrackEntity>,
        deviations: List<DeviationEntry>,
    )

    @Query("SELECT deviations.* FROM deviations WHERE deviationId = :id")
    fun observeDeviation(id: String): Flow<DeviationEntry>

    @Query(
        """SELECT deviations.* 
        FROM tracks_deviations tracks 
        INNER JOIN deviations ON tracks.deviationId = deviations.deviationId 
        WHERE tracks.track = :track""",
    )
    fun observeTrackDeviations(track: String): Flow<List<DeviationEntry>>

    @Query("SELECT * FROM  deviations")
    fun observeAll(): Flow<List<DeviationEntry>>

    @Transaction
    @Query(
        """SELECT * FROM tracks_deviations
        WHERE track = :track
        ORDER BY id, nextPage LIMIT :count OFFSET :offset""",
    )
    fun observeTrack(track: String, count: Int, offset: Int): Flow<List<TrackWithDeviation>>
}
