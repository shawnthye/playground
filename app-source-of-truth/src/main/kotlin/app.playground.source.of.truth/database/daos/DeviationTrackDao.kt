package app.playground.source.of.truth.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.playground.source.of.truth.database.EntityDao
import app.playground.source.of.truth.database.entities.DeviationTrack
import app.playground.source.of.truth.database.entities.TrackWithDeviation

@Dao
abstract class DeviationTrackDao : EntityDao<DeviationTrack>() {

    @Transaction
    @Query("SELECT * FROM deviation_tracks WHERE track = :track ORDER BY id")
    abstract fun paging(track: String): PagingSource<Int, TrackWithDeviation>
}
