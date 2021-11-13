package feature.playground.deviant.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.playground.entities.Deviation
import feature.playground.deviant.databinding.DeviationItemBinding

class DeviantArtsAdapter(
    private val onClickListener: OnClickListener,
) : ListAdapter<Deviation, DeviationArtViewHolder>(Diff) {

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
    fun bind(deviation: Deviation) {
        binding.deviation = deviation
        binding.executePendingBindings()
    }
}

object Diff : DiffUtil.ItemCallback<Deviation>() {
    override fun areItemsTheSame(oldItem: Deviation, newItem: Deviation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Deviation, newItem: Deviation) = oldItem == newItem
}
