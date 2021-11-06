package feature.playground.deviant.data

// import api.art.deviant.DeviantArt
import api.art.deviant.DeviantArt
import app.playground.entities.Deviation
import core.playground.domain.Result
import core.playground.domain.asNetworkBoundResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviantRepository @Inject constructor(
    private val deviantArt: DeviantArt,
    private val deviationDao: DeviationDao,
) {
    fun observePopular(): Flow<Result<List<Deviation>>> = deviantArt
        .api
        .popular(null)
        .asNetworkBoundResource(
            query = deviationDao.observeAll(),
            shouldFetch = { true },
        ) { response ->

            val entities = response.results.map {
                Deviation(
                    id = it.deviationid,
                    url = it.url,
                    title = it.title,
                    imageSrc = it.content.src,
                    imageHeight = it.content.height,
                    imageWidth = it.content.width,
                    track = null,
                )
            }

            deviationDao.insertAll(entities)
        }
}
