package feature.playground.deviant.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.playground.entities.DeviationEntities
import feature.playground.deviant.databinding.DeviationItemBinding

class DeviantArtsAdapter(
    private val onClickListener: OnClickListener,
) : ListAdapter<DeviationEntities, DeviationArtViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviationArtViewHolder {

        val binding = DeviationItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false,
        )
        binding.onClickListener = onClickListener

        return DeviationArtViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviationArtViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnClickListener {
        fun onClicked(id: String)
    }
}

class DeviationArtViewHolder(
    private val binding: DeviationItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(deviation: DeviationEntities) {
        binding.deviation = deviation
        binding.executePendingBindings()
    }
}

object Diff : DiffUtil.ItemCallback<DeviationEntities>() {
    override fun areItemsTheSame(oldItem: DeviationEntities, newItem: DeviationEntities): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DeviationEntities, newItem: DeviationEntities) = oldItem == newItem
}
