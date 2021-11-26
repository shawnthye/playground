package app.playground.source.of.truth.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Transaction
import androidx.room.Update

abstract class EntityDao<E : AppEntity> {

    @Insert
    abstract suspend fun insert(entity: E): Long

    @Insert
    abstract suspend fun insert(vararg entity: E)

    @Insert
    abstract suspend fun insert(entities: List<E>)

    @Insert(onConflict = IGNORE)
    abstract suspend fun insertIgnore(entity: E): Long

    @Insert(onConflict = IGNORE)
    abstract suspend fun insertIgnore(vararg entity: E)

    @Insert(onConflict = IGNORE)
    abstract suspend fun insertIgnore(entities: List<E>)

    @Insert(onConflict = REPLACE)
    abstract suspend fun replace(entity: E): Long

    @Insert(onConflict = REPLACE)
    abstract suspend fun replace(vararg entity: E)

    @Insert(onConflict = REPLACE)
    abstract suspend fun replace(entities: List<E>)

    @Update
    abstract suspend fun update(entity: E)

    @Delete
    abstract suspend fun deleteEntity(entity: E): Int

    @Transaction
    open suspend fun withTransaction(tx: suspend () -> Unit) = tx()

    @Transaction
    open suspend fun upsert(entity: E): Long {
        val id = insertIgnore(entity)
        return if (id == -1L) {
            update(entity)
            entity.id
        } else {
            id
        }
    }

    @Transaction
    open suspend fun upsert(entities: List<E>) {
        entities.forEach {
            upsert(it)
        }
    }

    @Transaction
    open suspend fun upsert(vararg entities: E) {
        upsert(entities.toList())
    }
}
