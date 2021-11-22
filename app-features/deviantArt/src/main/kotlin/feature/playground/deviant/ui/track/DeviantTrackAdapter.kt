package feature.playground.deviant.ui.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import feature.playground.deviant.databinding.DeviationItemBinding

class DeviantTrackAdapter(
    private val onClickListener: OnClickListener,
) : ListAdapter<app.playground.source.of.truth.database.entities.TrackWithDeviation, DeviationViewHolder>(
    Diff,
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviationViewHolder {

        val binding = DeviationItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false,
        )
        binding.onClickListener = onClickListener

        return DeviationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviationViewHolder, position: Int) {
        holder.bind(getItem(position).deviation)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).deviation.deviationId.hashCode().toLong()
    }

    interface OnClickListener {
        fun onClicked(id: String)
    }

    init {
        setHasStableIds(true)
    }
}

class DeviationViewHolder(
    private val binding: DeviationItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(deviation: app.playground.source.of.truth.database.entities.Deviation) {
        binding.deviation = deviation
        binding.executePendingBindings()
    }
}

object Diff :
    DiffUtil.ItemCallback<app.playground.source.of.truth.database.entities.TrackWithDeviation>() {
    override fun areItemsTheSame(
        oldItem: app.playground.source.of.truth.database.entities.TrackWithDeviation,
        newItem: app.playground.source.of.truth.database.entities.TrackWithDeviation,
    ) = oldItem.deviation.deviationId == newItem.deviation.deviationId

    override fun areContentsTheSame(
        oldItem: app.playground.source.of.truth.database.entities.TrackWithDeviation,
        newItem: app.playground.source.of.truth.database.entities.TrackWithDeviation,
    ) = oldItem.deviation.deviationId == newItem.deviation.deviationId &&
        oldItem.deviation.coverUrl == newItem.deviation.coverUrl
}
