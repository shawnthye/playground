package feature.playground.deviant.art.track.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import core.playground.ui.asUiMessage
import core.playground.ui.string
import feature.playground.deviant.art.track.R
import feature.playground.deviant.art.track.databinding.NetworkStateItemBinding

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
        return when (loadState) {
            is LoadState.Loading -> R.id.load_state_footer_view_type_loading
            is LoadState.Error -> R.id.load_state_footer_view_type_error
            is LoadState.NotLoading -> R.id.load_state_footer_view_type_empty
        }
    }

    /**
     * Enable this will make sure the next page shift up, so user aware the next page is loaded
     * But this will cause the List not at the top on REFRESH
     *
     * Disable this will result in RecyclerView always shift down after next page loaded
     * see this https://github.com/android/architecture-components-samples/issues/1037
     *
     *
     * TODO: find other alternative
     */
    // override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
    //     return super.displayLoadStateAsItem(loadState) ||
    //         (loadState is LoadState.NotLoading && !loadState.endOfPaginationReached)
    // }
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

            if (loadState is LoadState.Error) {
                errorMsg.text = loadState.error.asUiMessage().string(errorMsg.context)
            } else {
                errorMsg.text = null
            }

            executePendingBindings()
        }
    }
}
