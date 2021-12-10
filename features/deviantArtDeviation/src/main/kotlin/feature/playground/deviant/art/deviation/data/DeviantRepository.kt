package feature.playground.deviant.art.deviation.data

import app.playground.store.database.daos.DeviationDao
import app.playground.store.database.entities.Deviation
import core.playground.domain.Result
import core.playground.domain.asNetworkBoundResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DeviationRepository @Inject constructor(
    private val dao: DeviationDao,
    private val dataSource: DeviationDataSource,
) {

    fun observeDeviation(id: String): Flow<Result<Deviation>> {
        return dataSource.getDeviation(id).asNetworkBoundResult(
            query = dao.observeDeviation(id),
            shouldFetch = { true },
        ) {
            dao.insert(it)
        }
    }
}
