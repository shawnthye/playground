package feature.playground.deviant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.databinding.DeviationDetailBinding

@AndroidEntryPoint
class DeviationFragment : Fragment() {

    // private val model: DeviationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = DeviationDetailBinding.inflate(inflater, container, false).apply {
        // viewModel = model
    }.root
}
