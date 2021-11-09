package core.playground.ui

import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding

fun ComponentActivity.setContentView(binding: ViewDataBinding) {
    binding.lifecycleOwner = this
    setContentView(binding.root)
}

fun ComponentActivity.setContentView(binding: ViewBinding) {
    setContentView(binding.root)
}
