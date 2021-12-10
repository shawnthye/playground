package core.playground.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar

interface NavigationHost {
    fun registerToolbarWithNavigation(toolbar: Toolbar)
}

abstract class DeviantArtNavigationFragment : Fragment() {

    private var navigationHost: NavigationHost? = null

    abstract override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (view !is ViewGroup) return

        findToolbar(view)?.run {
            navigationHost?.registerToolbarWithNavigation(this)
        }
    }

    override fun onAttach(context: Context) {
        navigationHost = context as? NavigationHost
        super.onAttach(context)
    }

    override fun onDetach() {
        navigationHost = null
        super.onDetach()
    }

    private fun findToolbar(view: ViewGroup): MaterialToolbar? {

        if (view.childCount == 0) {
            return null
        }

        for (child in view.children) {
            if (child is MaterialToolbar) {
                return child
            } else if (child is ViewGroup) {
                val toolbar = findToolbar(child)
                if (toolbar != null) {
                    return toolbar
                }
            }
        }

        return null
    }
}
