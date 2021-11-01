package app.playground.module.deviant.data

import api.art.deviant.DeviantArt
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviantRepository @Inject constructor(
    val deviantArt: DeviantArt,
    val deviationDao: DeviationDao,
) {
}
