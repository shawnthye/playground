package feature.playground.deviant.ui.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.DeviantArtNavigationDirections
import feature.playground.deviant.R
import feature.playground.deviant.databinding.DeviantTrackBinding
import feature.playground.deviant.ui.DeviantArtNavigationFragment
import feature.playground.deviant.widget.SlideInItemAnimator
import feature.playground.deviant.widget.SpaceDecoration
import feature.playground.deviant.widget.onCreateViewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeviantTrackFragment : DeviantArtNavigationFragment() {

    private val model: DeviantTrackViewModel by viewModels()

    private lateinit var binding: DeviantTrackBinding
    private lateinit var pagingAdapter: TrackPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = onCreateViewBinding(DeviantTrackBinding.inflate(inflater, container, false)) {
        binding = this
        viewModel = model

        pagingAdapter = TrackPagingAdapter(model)

        // val header = DeviationTrackLoadStateAdapter {
        //     Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
        // }

        val footer = DeviationTrackLoadStateAdapter {
            Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
        }

        pagingAdapter.addLoadStateListener { loadStates ->
            // header.loadState = loadStates.refresh
            footer.loadState = loadStates.append
        }

        val concatAdapter = ConcatAdapter(
            ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build(),
            // header,
            pagingAdapter,
            footer,
        )

        val space = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        deviations.run {
            itemAnimator = SlideInItemAnimator()
            // itemAnimator = null
            addItemDecoration(SpaceDecoration(space, space, space, space))

            adapter = concatAdapter
        }
        lifecycleScope.launchWhenCreated {
            model.pagingData.collectLatest { latest ->
                pagingAdapter.submitData(latest)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.idActions.collect {
                    findNavController().navigate(DeviantArtNavigationDirections.toDeviantDetail(it))
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = binding.deviations.layoutManager as GridLayoutManager
        val adapter = binding.deviations.adapter!!
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                return when (adapter.getItemViewType(position)) {
                    R.id.load_state_footer_view_type_error,
                    R.id.load_state_footer_view_type_loading,
                    R.id.load_state_footer_view_type_empty,
                    -> manager.spanCount

                    R.layout.deviation_item,
                    -> 1
                    else -> throw IllegalStateException("Invalid view type")
                }
            }
        }
    }
}
