package feature.playground.deviant.ui.deviation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.databinding.DeviationDetailBinding
import feature.playground.deviant.ui.DeviantArtNavigationFragment
import feature.playground.deviant.widget.onCreateViewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeviationDetail : DeviantArtNavigationFragment() {

    private val model: DeviationDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = onCreateViewBinding(DeviationDetailBinding.inflate(inflater, container, false)) {
        viewModel = model
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.snackbarMessageId.collect {
                    Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
