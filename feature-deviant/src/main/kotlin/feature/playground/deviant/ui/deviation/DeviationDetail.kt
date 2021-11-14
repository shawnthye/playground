package feature.playground.deviant.ui.deviation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import core.playground.ui.onCreateViewBinding
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.databinding.DeviationDetailBinding

@AndroidEntryPoint
class DeviationDetail : Fragment() {

    private val model: DeviationDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = onCreateViewBinding(DeviationDetailBinding.inflate(inflater, container, false)) {
        viewModel = model
    }
}
