package feature.playground.deviant.ui.track

import androidx.recyclerview.widget.DiffUtil
import app.playground.store.database.entities.TrackWithDeviation

object TrackAdapterItemCallback : DiffUtil.ItemCallback<TrackWithDeviation>() {
    override fun areItemsTheSame(
        oldItem: TrackWithDeviation,
        newItem: TrackWithDeviation,
    ) = oldItem.entry.id == newItem.entry.id

    override fun areContentsTheSame(
        oldItem: TrackWithDeviation,
        newItem: TrackWithDeviation,
    ) = oldItem.entry.deviationId == newItem.entry.deviationId &&
        oldItem.deviation.coverUrl == newItem.deviation.coverUrl &&
        oldItem.deviation.rippleColor == newItem.deviation.rippleColor
}
