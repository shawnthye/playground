package core.playground.ui

import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding

fun ComponentActivity.setContentView(binding: ViewDataBinding) {
    binding.lifecycleOwner = this
    setContentView(binding.root)
}
