package feature.playground.deviant.widget

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

internal fun <T : ViewDataBinding> Fragment.onCreateViewBinding(
    binding: T,
    block: (T.() -> Unit)? = null,
): View {
    binding.lifecycleOwner = viewLifecycleOwner
    block?.run { this(binding) }
    return binding.root
}
