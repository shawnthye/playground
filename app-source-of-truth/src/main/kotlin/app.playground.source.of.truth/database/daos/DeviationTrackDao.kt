package app.playground.source.of.truth.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.playground.source.of.truth.database.EntityDao
import app.playground.source.of.truth.database.entities.DeviationTrack
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
    ): Flow<List<app.playground.source.of.truth.database.entities.TrackWithDeviation>>
}
