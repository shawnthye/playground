package app.playground.store.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import app.playground.store.database.EntityDao
import app.playground.store.database.entities.Discovery
import app.playground.store.database.entities.DiscoveryPostEntry
import app.playground.store.database.entities.Post

@Dao
abstract class DiscoveryDao : EntityDao<DiscoveryPostEntry>() {

    @Transaction
    @Query("SELECT * FROM discovery ORDER BY id")
    abstract fun pagingSource(): PagingSource<Int, Discovery>

    @Query("DELETE FROM discovery")
    abstract suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun replace(entries: List<DiscoveryPostEntry>, post: List<Post>)
}
