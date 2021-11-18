package core.playground.ui

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

fun <T : ViewDataBinding> Fragment.onCreateViewBinding(
    binding: T,
    block: (T.() -> Unit)? = null,
): View {
    binding.lifecycleOwner = viewLifecycleOwner
    block?.run { this(binding) }
    return binding.root
}
