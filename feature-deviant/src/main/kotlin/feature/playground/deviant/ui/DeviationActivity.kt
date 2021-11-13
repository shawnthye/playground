package feature.playground.deviant.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import core.playground.ui.setContentView

import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.databinding.DeviationDetailBinding

@AndroidEntryPoint
class DeviationActivity : AppCompatActivity() {

    private val model: DeviationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(DeviationDetailBinding.inflate(layoutInflater)) {
            viewModel = model
        }
    }

    companion object {

        fun intentFor(context: Context, id: String): Intent {
            return Intent(context, DeviationActivity::class.java).apply {
                putExtra(DeviationViewModel.EXTRA_ID, id)
            }
        }
    }
}
