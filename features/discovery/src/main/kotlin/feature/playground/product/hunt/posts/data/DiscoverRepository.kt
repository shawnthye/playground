package feature.playground.product.hunt.posts.data

import app.playground.store.database.daos.DiscoveryDao
import app.playground.store.database.daos.PostDao
import core.playground.data.execute
import javax.inject.Inject

internal class DiscoverRepository @Inject constructor(
    private val discoveryDao: DiscoveryDao,
    private val postDao: PostDao,
    private val discoverDataSource: DiscoverDataSource,
) {

    fun pagingSource() = discoveryDao.pagingSource()

    suspend fun queryNext(nextPage: String?): Boolean {
        val query = discoverDataSource.query(nextPage).execute() ?: return true

        discoveryDao.withTransaction {
            if (nextPage.isNullOrBlank()) {
                discoveryDao.deleteAll()
            }

            discoveryDao.insert(query.map { it.entry })
            postDao.insert(query.map { it.post })
        }

        // return query.lastOrNull()?.entry?.nextPage.isNullOrBlank()

        return true
    }
}
