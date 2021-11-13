package feature.playground.deviant.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.playground.entities.DeviationEntities
import app.playground.entities.PopularDeviationEntities
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deviation: DeviationEntities)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPopular(populars: List<PopularDeviationEntities>, deviations: List<DeviationEntities>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(deviations: List<DeviationEntities>)

    @Query("DELETE FROM deviations")
    suspend fun deleteAll()

    // @Transaction
    // suspend fun refresh(){
    //     deleteAll()
    //
    // }

    @Query("SELECT deviations.* FROM popular_deviations INNER JOIN deviations")
    fun getPopular(): Flow<DeviationEntities>

    @Query("SELECT * FROM  deviations")
    fun observeAll(): Flow<List<DeviationEntities>>
}
