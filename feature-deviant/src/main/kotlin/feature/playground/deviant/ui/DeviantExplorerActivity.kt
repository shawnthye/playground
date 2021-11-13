package feature.playground.deviant.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import app.playground.entities.DeviationEntity
import core.playground.ui.setContentView
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.databinding.DeviantArtBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeviantExplorerActivity : AppCompatActivity() {

    private val viewModel: DeviantArtViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DeviantArtBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        setContentView(binding)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.idActions.collect {
                    startActivity(DeviationActivity.intentFor(this@DeviantExplorerActivity, it))
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

@BindingAdapter(value = ["deviations", "onClickListener"])
fun deviations(
    recyclerView: RecyclerView,
    list: List<DeviationEntity>?,
    onClickListener: DeviantArtsAdapter.OnClickListener,
) {
    list ?: return

    if (recyclerView.adapter == null) {
        recyclerView.adapter = DeviantArtsAdapter(onClickListener)
    }
    recyclerView.setOnClickListener { }
    (recyclerView.adapter as DeviantArtsAdapter).apply {
        submitList(list)
    }
}
