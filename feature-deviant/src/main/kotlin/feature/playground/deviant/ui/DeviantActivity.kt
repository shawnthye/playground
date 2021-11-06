package feature.playground.deviant.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import core.playground.ui.setContentView
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.databinding.DeviantBinding

@AndroidEntryPoint
class DeviantActivity : AppCompatActivity() {

    val viewModel: DeviantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DeviantBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        setContentView(binding)
    }
}
