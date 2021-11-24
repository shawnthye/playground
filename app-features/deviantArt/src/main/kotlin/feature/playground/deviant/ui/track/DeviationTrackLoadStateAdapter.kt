package feature.playground.deviant.ui.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import feature.playground.deviant.R
import feature.playground.deviant.databinding.NetworkStateItemBinding

class DeviationTrackLoadStateAdapter(
    private val retry: () -> Unit,
) : LoadStateAdapter<NetworkStateViewHolder>() {

    override fun onBindViewHolder(holder: NetworkStateViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): NetworkStateViewHolder = NetworkStateViewHolder(parent) {
        retry()
    }

    override fun getStateViewType(loadState: LoadState): Int {
        return R.layout.network_state_item
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return super.displayLoadStateAsItem(loadState) || loadState is LoadState.NotLoading
    }
}

class NetworkStateViewHolder(
    parent: ViewGroup,
    private val binding: NetworkStateItemBinding = NetworkStateItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false,
    ),
    private val retry: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener {
            retry()
        }
    }

    fun bindTo(loadState: LoadState) {
        binding.run {
            progressBar.isVisible = loadState is LoadState.Loading
            retryButton.isVisible = loadState is LoadState.Error
            errorMsg.isVisible = loadState is LoadState.Error
            errorMsg.text = (loadState as? LoadState.Error)?.error?.message
            executePendingBindings()
        }
    }
}
