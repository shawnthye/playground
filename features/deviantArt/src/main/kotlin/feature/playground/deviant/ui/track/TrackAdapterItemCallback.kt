package feature.playground.deviant.ui.track

import androidx.recyclerview.widget.DiffUtil
import app.playground.source.of.truth.database.entities.TrackWithDeviation

object TrackAdapterItemCallback : DiffUtil.ItemCallback<TrackWithDeviation>() {
    override fun areItemsTheSame(
        oldItem: TrackWithDeviation,
        newItem: TrackWithDeviation,
    ) = oldItem.entry.id == newItem.entry.id

    override fun areContentsTheSame(
        oldItem: TrackWithDeviation,
        newItem: TrackWithDeviation,
    ) = oldItem.entry.deviationId == newItem.entry.deviationId &&
        oldItem.relation.coverUrl == newItem.relation.coverUrl
}
