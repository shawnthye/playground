package app.playground.module.deviant.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.playground.entities.Deviation
import app.playground.entities.PopularDeviation
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deviation: Deviation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(populars: List<PopularDeviation>, deviations: List<Deviation>)

    @Query("SELECT deviations.* FROM popular_deviations INNER JOIN deviations")
    fun getPopular(): Flow<Deviation>
}
