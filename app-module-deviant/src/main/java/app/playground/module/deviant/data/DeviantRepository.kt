package app.playground.module.deviant.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviantRepository @Inject constructor(
    val deviationDao: DeviationDao,
) {
}
