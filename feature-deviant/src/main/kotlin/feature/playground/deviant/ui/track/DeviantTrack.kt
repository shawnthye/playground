package feature.playground.deviant.ui.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import app.playground.entities.DeviationEntity
import core.playground.ui.onCreateViewBinding
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.R
import feature.playground.deviant.databinding.DeviantTrackBinding
import feature.playground.deviant.ui.deviation.DeviationDetailViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeviantTrack : Fragment() {

    private val model: DeviantTrackViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = onCreateViewBinding(DeviantTrackBinding.inflate(inflater, container, false)) {
        viewModel = model

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.idActions.collect {
                    findNavController().navigate(
                        R.id.deviant_detail,
                        bundleOf(DeviationDetailViewModel.ARGS_DEVIATION_ID to it),
                    )
                }
            }
        }
    }
}

@BindingAdapter(value = ["deviations", "onClickListener"])
fun deviations(
    recyclerView: RecyclerView,
    list: List<DeviationEntity>?,
    onClickListener: DeviantTrackAdapter.OnClickListener,
) {
    list ?: return

    if (recyclerView.adapter == null) {
        recyclerView.adapter = DeviantTrackAdapter(onClickListener)
    }
    recyclerView.setOnClickListener { }
    (recyclerView.adapter as DeviantTrackAdapter).apply {
        submitList(list)
    }
}
