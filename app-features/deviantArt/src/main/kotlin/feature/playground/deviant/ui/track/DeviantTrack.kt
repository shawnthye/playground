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
class DeviantTrack : DeviantArtNavigationFragment() {

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

        val loadStateAdapter = DeviationTrackLoadStateAdapter {
            Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
        }

        pagingAdapter = TrackPagingAdapter(model)

        val space = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        deviations.run {
            itemAnimator = SlideInItemAnimator()
            addItemDecoration(SpaceDecoration(space, space, space, space))
            val aaa = pagingAdapter.withLoadStateHeaderAndFooter(
                header = loadStateAdapter,
                footer = DeviationTrackLoadStateAdapter {
                    Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
                },
            )
            adapter = aaa
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
                // Timber.i("${binding.deviations.adapter?.getItemViewType(position)}")
                return if (0 == binding.deviations.adapter!!.getItemViewType(position)) {
                    1
                } else {
                    manager.spanCount
                }
            }
        }
    }
}
