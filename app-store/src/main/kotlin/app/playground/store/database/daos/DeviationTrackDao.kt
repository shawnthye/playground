package app.playground.store.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.playground.store.database.EntityDao
import app.playground.store.database.entities.DeviationTrack
import app.playground.store.database.entities.TrackWithDeviation

@Dao
abstract class DeviationTrackDao : EntityDao<DeviationTrack>() {

    @Transaction
    @Query("SELECT * FROM deviation_tracks WHERE track = :track ORDER BY id")
    abstract fun pagingSource(track: String): PagingSource<Int, TrackWithDeviation>

    @Query("DELETE FROM deviation_tracks WHERE track = :track")
    abstract suspend fun deleteTrack(track: String)
}
