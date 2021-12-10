package feature.playground.deviant.art.deviation.data

import api.art.deviant.DeviantArt
import app.playground.store.database.entities.Deviation
import app.playground.store.mappers.DeviationToEntity
import core.playground.data.FlowResponse
import core.playground.data.withMapper

internal interface DeviationDataSource {
    fun getDeviation(id: String): FlowResponse<Deviation>
}

internal class RemoteDeviationDataSource constructor(
    private val deviantArt: DeviantArt,
    private val deviationToEntity: DeviationToEntity,
) : DeviationDataSource {
    override fun getDeviation(id: String): FlowResponse<Deviation> {
        return deviantArt.api.deviation(id).withMapper(deviationToEntity)
    }
}
