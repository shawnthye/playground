package app.playground.entities.mappers

import api.art.deviant.model.DeviationResult
import app.playground.entities.entries.DeviationEntry
import core.playground.data.Mapper
import javax.inject.Inject

class DeviationResultToEntity @Inject constructor(
    private val deviationToEntity: DeviationToEntity,
) : Mapper<DeviationResult, List<DeviationEntry>>() {
    override suspend fun map(
        from: DeviationResult,
    ): List<DeviationEntry> = from.results.filter { it.findCover() != null }.map {
        deviationToEntity(it)
    }
}
