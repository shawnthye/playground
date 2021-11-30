package feature.playground.deviant.ui.deviation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import feature.playground.deviant.R
import feature.playground.deviant.databinding.DeviationDetailListAuthorBinding
import feature.playground.deviant.databinding.DeviationDetailListHeaderBinding

internal class DeviationDetailAdapter(
    private val lifecycleOwner: LifecycleOwner,
) : ListAdapter<DeviationUiModel, ViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = when (viewType) {
            R.layout.deviation_detail_list_header -> {
                DeviationDetailListHeaderBinding.inflate(inflater, parent, false)
            }
            R.layout.deviation_detail_list_author -> {
                DeviationDetailListAuthorBinding.inflate(inflater, parent, false)
            }
            else -> throw IllegalStateException("invalid view type")
        }

        binding.lifecycleOwner = lifecycleOwner

        return ViewHolder(binding = binding)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private object Diff : DiffUtil.ItemCallback<DeviationUiModel>() {
    override fun areItemsTheSame(oldItem: DeviationUiModel, newItem: DeviationUiModel): Boolean {
        return oldItem.viewType == newItem.viewType
    }

    override fun areContentsTheSame(oldItem: DeviationUiModel, newItem: DeviationUiModel): Boolean {
        return oldItem == newItem
    }
}

internal class ViewHolder(
    val binding: ViewDataBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiModel: DeviationUiModel) {
        when (binding) {
            is DeviationDetailListHeaderBinding -> {
                binding.model = uiModel as DeviationUiModel.Header
            }
            is DeviationDetailListAuthorBinding -> {
                binding.model = uiModel as DeviationUiModel.Author
            }
        }

        binding.executePendingBindings()
    }
}
