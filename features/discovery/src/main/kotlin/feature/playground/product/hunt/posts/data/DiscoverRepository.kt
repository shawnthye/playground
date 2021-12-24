package feature.playground.product.hunt.posts.data

import app.playground.store.DatabaseTransactionRunner
import app.playground.store.database.daos.DiscoveryDao
import core.playground.data.execute
import javax.inject.Inject

internal class DiscoverRepository @Inject constructor(
    private val discoveryDao: DiscoveryDao,
    private val discoverDataSource: DiscoverDataSource,
    private val transactionRunner: DatabaseTransactionRunner,
) {

    fun pagingSource() = discoveryDao.pagingSource()

    suspend fun queryNext(nextPage: String?): Boolean {

        val query = discoverDataSource.query(nextPage).execute() ?: return true

        transactionRunner {
            if (nextPage.isNullOrBlank()) {
                discoveryDao.deleteAll()
            }

            val entries = query.map { it.entry }
            val posts = query.map { it.post }
            discoveryDao.replace(entries, posts)
        }

        return query.lastOrNull()?.entry?.nextPage.isNullOrBlank()
    }
}
