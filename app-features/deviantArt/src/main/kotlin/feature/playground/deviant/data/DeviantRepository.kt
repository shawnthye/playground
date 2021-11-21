package feature.playground.deviant.data

import app.playground.core.data.daos.DeviationDao
import app.playground.entities.DeviationEntity
import app.playground.entities.mappers.DeviationToEntity
import core.playground.domain.Result
import core.playground.domain.asNetworkBoundResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviantRepository @Inject constructor(
    private val deviantDataSource: DeviantDataSource,
    private val deviationDao: DeviationDao,
    private val deviationToEntity: DeviationToEntity,
) {

    fun observeDeviation(id: String): Flow<Result<DeviationEntity>> = deviantDataSource
        .fetchDeviation(id)
        .asNetworkBoundResource(
            query = deviationDao.observeDeviation(id),
            shouldFetch = { true },
        ) {
            deviationDao.insert(deviationToEntity(it))
        }

    fun observePopular(): Flow<Result<List<DeviationEntity>>> = deviantDataSource
        .fetchPopular()
        .asNetworkBoundResource(
            query = deviationDao.observeAll(),
            shouldFetch = { true },
        ) { response ->
            val entities = response.results.map { deviationToEntity(it) }
            deviationDao.insertAll(entities)
        }
}
