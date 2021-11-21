package app.playground.entities.mappers

import api.art.deviant.model.DeviationResult
import app.playground.entities.DeviationEntity
import core.playground.data.Mapper
import javax.inject.Inject

class DeviationResultToEntity @Inject constructor(
    private val deviationToEntity: DeviationToEntity,
) : Mapper<DeviationResult, List<DeviationEntity>>() {
    override suspend fun map(
        from: DeviationResult,
    ): List<DeviationEntity> = from.results.filter { it.hasImage }.map { deviationToEntity(it) }
}
