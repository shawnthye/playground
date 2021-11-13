package feature.playground.deviant.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import core.playground.ui.setContentView
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.databinding.DeviationDetailBinding

@AndroidEntryPoint
class DeviationActivity : AppCompatActivity() {

    // private val model: DeviationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(DeviationDetailBinding.inflate(layoutInflater)) {
            id = intent.extras?.getString(EXTRA_ID)
        }
    }

    companion object {

        private const val EXTRA_ID = "extra.id"

        fun intentFor(context: Context, id: String): Intent {
            return Intent(context, DeviationActivity::class.java).apply {
                putExtra(EXTRA_ID, id)
            }
        }
    }
}
