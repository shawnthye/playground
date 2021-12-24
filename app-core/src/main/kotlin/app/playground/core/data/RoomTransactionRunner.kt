package app.playground.core.data

import androidx.room.withTransaction
import app.playground.store.DatabaseTransactionRunner
import javax.inject.Inject

internal class RoomTransactionRunner @Inject constructor(
    private val db: AppDatabase,
) : DatabaseTransactionRunner {
    override suspend fun <R> invoke(block: suspend () -> R): R = db.withTransaction { block() }
}
