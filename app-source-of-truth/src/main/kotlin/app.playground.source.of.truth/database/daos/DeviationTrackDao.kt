package app.playground.source.of.truth.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.playground.source.of.truth.database.EntityDao
import app.playground.source.of.truth.database.entities.DeviationTrack
import app.playground.source.of.truth.database.entities.TrackWithDeviation
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

    @Transaction
    @Query("SELECT * FROM deviation_tracks WHERE track = :track ORDER BY id, nextPage")
    abstract fun paging(track: String): PagingSource<Int, TrackWithDeviation>

    @Transaction
    @Query("SELECT * FROM deviation_tracks WHERE track = 'NEWEST' ORDER BY id, nextPage")
    abstract fun paging2(): PagingSource<Int, TrackWithDeviation>
}
