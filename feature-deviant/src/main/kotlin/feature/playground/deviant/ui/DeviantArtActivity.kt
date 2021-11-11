package feature.playground.deviant.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import core.playground.ui.setContentView
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.databinding.DeviantArtBinding

@AndroidEntryPoint
class DeviantArtActivity : AppCompatActivity() {

    private val viewModel: DeviantArtViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DeviantArtBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        setContentView(binding)
    }
}
