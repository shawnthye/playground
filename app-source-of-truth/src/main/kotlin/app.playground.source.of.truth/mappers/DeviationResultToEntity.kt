package app.playground.source.of.truth.mappers

import api.art.deviant.model.DeviationResult
import app.playground.source.of.truth.database.entities.Deviation
import core.playground.data.Mapper
import javax.inject.Inject

class DeviationResultToEntity @Inject constructor(
    private val deviationToEntity: DeviationToEntity,
) : Mapper<DeviationResult, List<Deviation>>() {
    override suspend fun parse(
        from: DeviationResult,
    ): List<Deviation> = from.results.filter { it.findCover() != null }.map {
        deviationToEntity(it)
    }
}
