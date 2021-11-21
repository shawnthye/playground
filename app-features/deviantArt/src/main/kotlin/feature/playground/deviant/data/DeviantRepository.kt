package feature.playground.deviant.data

import app.playground.core.data.daos.DeviationDao
import app.playground.entities.DeviationEntity
import core.playground.domain.Result
import core.playground.domain.asNetworkBoundResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviantRepository @Inject constructor(
    private val deviationDataSource: DeviationDataSource,
    private val deviationDao: DeviationDao,
) {

    fun observeDeviation(id: String): Flow<Result<DeviationEntity>> = deviationDao
        .observeDeviation(id)
        .asNetworkBoundResult(
            remote = deviationDataSource.getDeviation(id),
            shouldFetch = { true },
        ) {
            deviationDao.insert(it)
        }

    fun observePopular(): Flow<Result<List<DeviationEntity>>> = deviationDao
        .observePopular()
        .asNetworkBoundResult(
            remote = deviationDataSource.getPopularDeviations(),
            shouldFetch = { true },
        ) {
            deviationDao.insertPopular(it.first, it.second)
        }
}
