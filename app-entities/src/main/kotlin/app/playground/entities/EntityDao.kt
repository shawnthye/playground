package app.playground.entities

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Transaction
import androidx.room.Update

abstract class EntityDao<E, ID> where E : Entity<out ID> {

    @Insert
    abstract suspend fun insert(entity: E): ID

    @Insert
    abstract suspend fun insertAll(vararg entity: E)

    @Insert
    abstract suspend fun insertAll(entities: List<E>)

    @Insert(onConflict = REPLACE)
    abstract suspend fun replace(entity: E): ID

    @Insert(onConflict = REPLACE)
    abstract suspend fun replaceAll(vararg entity: E)

    @Insert(onConflict = REPLACE)
    abstract suspend fun replaceAll(entities: List<E>)

    @Update
    abstract suspend fun update(entity: E)

    @Delete
    abstract suspend fun deleteEntity(entity: E): Int

    @Transaction
    open suspend fun withTransaction(tx: suspend () -> Unit) = tx()

    suspend fun insertOrUpdate(entity: E, exist: (entity: E) -> Boolean): ID {
        return if (!exist(entity)) {
            insert(entity)
        } else {
            update(entity)
            entity.id
        }
    }

    @Transaction
    open suspend fun insertOrUpdate(entities: List<E>) {
        entities.forEach {
            insertOrUpdate(it, { true })
        }
    }
}
