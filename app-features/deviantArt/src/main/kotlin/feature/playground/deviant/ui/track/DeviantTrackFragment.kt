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
import androidx.recyclerview.widget.RecyclerView
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
import timber.log.Timber

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


        pagingAdapter = TrackPagingAdapter(model).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT
        }

        // val header = DeviationTrackLoadStateAdapter {
        //     Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
        // }

        val footer = DeviationTrackLoadStateAdapter {
            Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
        }

        pagingAdapter.addLoadStateListener { loadStates ->
            // header.loadState = loadStates.prepend
            footer.loadState = loadStates.append
        }

        val concatAdapter = ConcatAdapter(
            ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build(),
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
            model.resultState.collectLatest {
                pagingAdapter.submitData(it)
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
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                Timber.i("${(binding.deviations.adapter as ConcatAdapter).getItemViewType(position)}")
                return if (20 == binding.deviations.adapter!!.getItemViewType(position)) {
                    manager.spanCount
                } else {
                    1
                }
            }
        }
    }
}
