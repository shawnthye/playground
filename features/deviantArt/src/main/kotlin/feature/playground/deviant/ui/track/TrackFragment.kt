package feature.playground.deviant.ui.track

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
import app.playground.store.database.entities.TrackWithDeviation
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.DeviantArtNavigationDirections
import feature.playground.deviant.R
import feature.playground.deviant.databinding.DeviantTrackBinding
import feature.playground.deviant.ui.DeviantArtNavigationFragment
import feature.playground.deviant.widget.SlideInItemAnimator
import feature.playground.deviant.widget.SpaceDecoration
import feature.playground.deviant.widget.onCreateViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeviantTrackFragment : DeviantArtNavigationFragment() {

    private val model: TrackViewModel by viewModels()

    private lateinit var binding: DeviantTrackBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = onCreateViewBinding(DeviantTrackBinding.inflate(inflater, container, false)) {
        binding = this
        viewModel = model

        bindState(model.pagingData, model, model)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.idActions.collectLatest {
                    findNavController().navigate(DeviantArtNavigationDirections.toDeviantDetail(it))
                }
            }
        }
    }
}

private fun DeviantTrackBinding.bindState(
    pagingData: Flow<PagingData<TrackWithDeviation>>,
    onItemClickListener: TrackAdapter.OnItemClickListener,
    onPaletteListener: TrackAdapter.OnPaletteListener,
) {
    val space = deviations.resources.getDimensionPixelSize(R.dimen.grid_spacing)
    deviations.itemAnimator = SlideInItemAnimator()
    deviations.addItemDecoration(SpaceDecoration(space, space, space, space))

    val pagingAdapter = TrackAdapter(onItemClickListener, onPaletteListener)

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
