package core.playground.ui

import android.view.View
import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
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

fun <T : ViewDataBinding> Fragment.onCreateViewBinding(
    binding: T,
    block: (T.() -> Unit)? = null,
): View {
    binding.lifecycleOwner = viewLifecycleOwner
    block?.run { this(binding) }
    return binding.root
}
