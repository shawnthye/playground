package feature.playground.deviant.data

// import api.art.deviant.DeviantArt
import api.art.deviant.DeviantArt
import app.playground.entities.DeviationEntities
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
    fun observePopular(): Flow<Result<List<DeviationEntities>>> = deviantArt
        .api
        .popular(null)
        .asNetworkBoundResource(
            query = deviationDao.observeAll(),
            shouldFetch = { true },
        ) { response ->

            val entities = response.results.map {
                DeviationEntities(
                    id = it.deviationid,
                    url = it.url,
                    title = it.title,
                    imageSrc = (it.thumbs.lastOrNull() ?: it.preview).src,
                    imageHeight = it.content.height,
                    imageWidth = it.content.width,
                    track = null,
                )
            }

            deviationDao.insertAll(entities)
        }
}
