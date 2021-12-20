package app.playground.store.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.playground.store.database.EntityDao
import app.playground.store.database.entities.Discovery
import app.playground.store.database.entities.DiscoveryPostEntry

@Dao
abstract class DiscoveryDao : EntityDao<DiscoveryPostEntry>() {

    @Transaction
    @Query("SELECT * FROM discovery ORDER BY id")
    abstract fun pagingSource(): PagingSource<Int, Discovery>

    @Query("DELETE FROM discovery")
    abstract suspend fun deleteAll()
}
