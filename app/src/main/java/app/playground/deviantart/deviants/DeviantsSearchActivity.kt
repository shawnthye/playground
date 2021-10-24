package app.playground.deviantart.deviants

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import app.playground.databinding.ActivityDeviantsSearchBinding

class DeviantsSearchActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDeviantsSearchBinding.inflate(layoutInflater) }

    private val viewModel: DeviantsSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}
