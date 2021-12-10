package feature.playground.deviant.art.track.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import app.playground.core.navigation.DeviantArtNavActions
import app.playground.store.database.entities.TrackWithDeviation
import core.playground.ui.DeviantArtNavigationFragment
import core.playground.ui.binding.onCreateViewBinding
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.art.track.R
import feature.playground.deviant.art.track.databinding.DeviantTrackBinding
import feature.playground.deviant.art.track.ui.widget.SpaceDecoration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DeviantTrackFragment : DeviantArtNavigationFragment() {

    @Inject
    private lateinit var navActions: DeviantArtNavActions

    private lateinit var binding: DeviantTrackBinding

    private val model: TrackViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = onCreateViewBinding(DeviantTrackBinding.inflate(inflater, container, false)) {
        binding = this
        viewModel = model

        bindState(model.pagingData, model)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.idActions.collectLatest {
                    findNavController()
                    navActions.toDeviation(findNavController(), it)
                }
            }
        }
    }
}

private fun DeviantTrackBinding.bindState(
    pagingData: Flow<PagingData<TrackWithDeviation>>,
    onItemClickListener: TrackAdapter.OnItemClickListener,
) {
    val space = deviations.resources.getDimensionPixelSize(R.dimen.grid_spacing)
    deviations.itemAnimator = SlideInItemAnimator()
    deviations.addItemDecoration(SpaceDecoration(space, space, space, space))

    val pagingAdapter = TrackAdapter(onItemClickListener)

    deviations.adapter = pagingAdapter.withFooter(
        DeviationTrackLoadStateAdapter {
            pagingAdapter.retry()
        },
    )

    deviations.adapter = pagingAdapter.withFooter(
        DeviationTrackLoadStateAdapter {
            pagingAdapter.retry()
        },
    )

    (deviations.layoutManager as GridLayoutManager).run {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val adapter = deviations.adapter!!

                return when (adapter.getItemViewType(position)) {
                    R.id.load_state_footer_view_type_error,
                    R.id.load_state_footer_view_type_loading,
                    R.id.load_state_footer_view_type_empty,
                    -> spanCount
                    R.layout.deviation_item -> 1
                    else -> throw IllegalStateException("Invalid view type")
                }
            }
        }
    }

    pagingAdapter.addLoadStateListener { loadState ->
        (loadState.refresh is LoadState.Loading && pagingAdapter.itemCount == 0).run {
            emptyProgress.isVisible = this
        }
    }

    swipeRefreshLayout.setOnRefreshListener {
        pagingAdapter.refresh()
    }

    val owner = lifecycleOwner!!

    owner.lifecycleScope.launchWhenCreated {
        pagingAdapter.loadStateFlow
            .distinctUntilChangedBy { it.refresh }
            .distinctUntilChanged { _, new -> new.refresh is LoadState.Loading }
            .collect { loadStates ->
                (loadStates.mediator?.refresh is LoadState.Loading).run {
                    swipeRefreshLayout.isRefreshing = this
                }
            }
    }

    owner.lifecycleScope.launch {
        owner.repeatOnLifecycle(Lifecycle.State.CREATED) {
            pagingData.collectLatest { latest ->
                pagingAdapter.submitData(latest)
            }
        }
    }
}
