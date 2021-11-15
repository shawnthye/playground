package feature.playground.deviant.ui.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.playground.entities.DeviationEntity
import feature.playground.deviant.databinding.DeviationItemBinding

class DeviantTrackAdapter(
    private val onClickListener: OnClickListener,
) : ListAdapter<DeviationEntity, DeviationViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviationViewHolder {

        val binding = DeviationItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false,
        )
        binding.onClickListener = onClickListener

        return DeviationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnClickListener {
        fun onClicked(id: String)
    }
}

class DeviationViewHolder(
    private val binding: DeviationItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(deviation: DeviationEntity) {
        binding.deviation = deviation
        binding.executePendingBindings()
    }
}

object Diff : DiffUtil.ItemCallback<DeviationEntity>() {
    override fun areItemsTheSame(
        oldItem: DeviationEntity,
        newItem: DeviationEntity,
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: DeviationEntity,
        newItem: DeviationEntity,
    ) = oldItem == newItem
}
