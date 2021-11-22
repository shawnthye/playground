package feature.playground.deviant.ui.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import app.playground.source.of.truth.database.entities.TrackWithDeviation
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.DeviantArtNavigationDirections
import feature.playground.deviant.R
import feature.playground.deviant.databinding.DeviantTrackBinding
import feature.playground.deviant.ui.DeviantArtNavigationFragment
import feature.playground.deviant.widget.SlideInItemAnimator
import feature.playground.deviant.widget.SpaceDecoration
import feature.playground.deviant.widget.onCreateViewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeviantTrack : DeviantArtNavigationFragment() {

    private val model: DeviantTrackViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = onCreateViewBinding(DeviantTrackBinding.inflate(inflater, container, false)) {
        viewModel = model

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.idActions.collect {
                    findNavController().navigate(DeviantArtNavigationDirections.toDeviantDetail(it))
                }
            }
        }
    }
}

@BindingAdapter(value = ["deviations", "onClickListener"])
fun deviations(
    recyclerView: RecyclerView,
    list: List<TrackWithDeviation>?,
    onClickListener: DeviantTrackAdapter.OnClickListener,
) {
    list ?: return

    if (recyclerView.adapter == null) {
        recyclerView.itemAnimator = SlideInItemAnimator()
        val space = recyclerView.context.resources.getDimensionPixelSize(R.dimen.grid_spacing)
        recyclerView.addItemDecoration(SpaceDecoration(space, space, space, space))
        recyclerView.adapter = DeviantTrackAdapter(onClickListener)
    }
    recyclerView.setOnClickListener { }
    (recyclerView.adapter as DeviantTrackAdapter).apply {
        submitList(list)
    }
}
