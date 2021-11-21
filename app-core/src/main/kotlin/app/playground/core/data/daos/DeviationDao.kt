package app.playground.core.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.playground.entities.DeviationEntity
import app.playground.entities.PopularDeviationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deviation: DeviationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPopular(
        populars: List<PopularDeviationEntity>,
        deviations: List<DeviationEntity>,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(deviations: List<DeviationEntity>)

    @Query("DELETE FROM deviations")
    suspend fun deleteAll()

    @Query("SELECT deviations.* FROM deviations WHERE deviationId = :id")
    fun observeDeviation(id: String): Flow<DeviationEntity>

    @Query("SELECT deviations.* FROM popular_deviations INNER JOIN deviations ON popular_deviations.deviationId = deviations.deviationId")
    fun observePopular(): Flow<List<DeviationEntity>>

    @Query("SELECT * FROM  deviations")
    fun observeAll(): Flow<List<DeviationEntity>>
}
