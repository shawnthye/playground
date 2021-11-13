package feature.playground.deviant.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.playground.entities.DeviationEntity
import app.playground.entities.PopularDeviationEntities
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deviation: DeviationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPopular(
        populars: List<PopularDeviationEntities>,
        deviations: List<DeviationEntity>,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(deviations: List<DeviationEntity>)

    @Query("DELETE FROM deviations")
    suspend fun deleteAll()

    // @Transaction
    // suspend fun refresh(){
    //     deleteAll()
    //
    // }

    @Query("SELECT deviations.* FROM deviations WHERE id = :id")
    fun observeDeviation(id: String): Flow<DeviationEntity>

    @Query("SELECT deviations.* FROM popular_deviations INNER JOIN deviations")
    fun getPopular(): Flow<List<DeviationEntity>>

    @Query("SELECT * FROM  deviations")
    fun observeAll(): Flow<List<DeviationEntity>>
}
