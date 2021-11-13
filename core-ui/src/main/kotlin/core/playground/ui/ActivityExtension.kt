package core.playground.ui

import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding

fun <T : ViewDataBinding> ComponentActivity.setContentView(
    binding: T,
    block: (T.() -> Unit)? = null,
): T {
    binding.lifecycleOwner = this
    setContentView(binding.root)
    block?.run { this(binding) }
    return binding
}

fun <T : ViewBinding> ComponentActivity.setContentView(
    binding: T,
    block: (T.() -> Unit)? = null,
): T {
    setContentView(binding.root)
    block?.run { this(binding) }
    return binding
}
