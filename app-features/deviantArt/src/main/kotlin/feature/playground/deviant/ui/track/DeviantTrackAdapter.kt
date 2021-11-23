package feature.playground.deviant.ui.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.playground.source.of.truth.database.entities.Deviation
import app.playground.source.of.truth.database.entities.TrackWithDeviation
import feature.playground.deviant.databinding.DeviationItemBinding

class DeviationViewHolder(
    parent: ViewGroup,
    onClickListener: TrackPagingAdapter.OnClickListener,
    private val binding: DeviationItemBinding = DeviationItemBinding.inflate(
        LayoutInflater.from(parent.context), parent, false,
    ),
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.onClickListener = onClickListener
    }

    fun bind(deviation: Deviation) {
        binding.deviation = deviation
        binding.executePendingBindings()
    }
}

object Diff :
    DiffUtil.ItemCallback<TrackWithDeviation>() {
    override fun areItemsTheSame(
        oldItem: TrackWithDeviation,
        newItem: TrackWithDeviation,
    ) = oldItem.deviation.deviationId == newItem.deviation.deviationId

    override fun areContentsTheSame(
        oldItem: TrackWithDeviation,
        newItem: TrackWithDeviation,
    ) = oldItem.deviation.deviationId == newItem.deviation.deviationId &&
        oldItem.deviation.coverUrl == newItem.deviation.coverUrl
}

class TrackPagingAdapter(
    private val onClickListener: OnClickListener,
) : PagingDataAdapter<TrackWithDeviation, DeviationViewHolder>(Diff) {

    interface OnClickListener {
        fun onClicked(id: String)
    }

    override fun onBindViewHolder(holder: DeviationViewHolder, position: Int) {
        getItem(position)?.deviation?.run {
            holder.bind(this)
        }
    }

    // override fun onBindViewHolder(
    //     holder: DeviationViewHolder,
    //     position: Int,
    //     payloads: MutableList<Any>
    // ) {
    //     // payloads.isNotEmpty()
    //     super.onBindViewHolder(holder, position, payloads)
    // }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DeviationViewHolder = DeviationViewHolder(parent, onClickListener)
}
