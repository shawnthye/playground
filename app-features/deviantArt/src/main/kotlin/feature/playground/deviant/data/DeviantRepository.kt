package feature.playground.deviant.data

import api.art.deviant.DeviantArt
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
    private val deviantArt: DeviantArt,
    private val deviationDao: DeviationDao,
    private val deviationToEntity: DeviationToEntity,
) {
    fun observeDeviation(id: String): Flow<DeviationEntity> = deviationDao.observeDeviation(id)

    fun observePopular(): Flow<Result<List<DeviationEntity>>> = deviantArt
        .api
        .popular(null)
        .asNetworkBoundResource(
            query = deviationDao.observeAll(),
            shouldFetch = { true },
        ) { response ->
            val entities = response.results.map { deviationToEntity(it) }
            deviationDao.insertAll(entities)
        }
}
