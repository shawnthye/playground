package app.playground.module.deviant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.playground.module.deviant.databinding.DeviationDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeviationFragment : Fragment() {

    private val model: DeviationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = DeviationDetailBinding.inflate(inflater, container, false).apply {
        viewModel = model
    }.root
}
