package app.playground.core.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.playground.entities.EntityDao
import app.playground.entities.entities.DeviationTrack
import app.playground.entities.entities.TrackWithDeviation
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DeviationTrackDao : EntityDao<DeviationTrack>() {

    @Transaction
    @Query(
        """SELECT * FROM deviation_tracks
        WHERE track = :track
        ORDER BY id, nextPage LIMIT :count OFFSET :offset""",
    )
    abstract fun observeDeviations(
        track: String,
        count: Int,
        offset: Int,
    ): Flow<List<TrackWithDeviation>>
}
