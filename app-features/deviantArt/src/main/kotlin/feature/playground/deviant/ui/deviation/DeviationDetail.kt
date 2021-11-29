package feature.playground.deviant.ui.deviation

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.palette.graphics.Palette
import coil.load
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.R
import feature.playground.deviant.databinding.DeviationDetailBinding
import feature.playground.deviant.ui.DeviantArtNavigationFragment
import feature.playground.deviant.widget.PaletteExtensions.createRipple
import feature.playground.deviant.widget.onCreateViewBinding
import feature.playground.deviant.widget.usePalette
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeviationDetail : DeviantArtNavigationFragment() {

    private val model: DeviationDetailViewModel by viewModels()
    private lateinit var binding: DeviationDetailBinding
    private var defaultToolbarIconColor: Int = Color.TRANSPARENT
    private var currentToolbarIconColor: Int = defaultToolbarIconColor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = onCreateViewBinding(DeviationDetailBinding.inflate(inflater, container, false)) {
        binding = this
        viewModel = model

        val value = TypedValue()

        if (toolbar.context.theme.resolveAttribute(
                com.google.android.material.R.attr.colorControlNormal,
                value,
                true,
            )
        ) {
            defaultToolbarIconColor = value.data
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            model.deviationState
                .mapNotNull { it.data?.imageUrl }
                .distinctUntilChanged()
                .collect { url ->
                    binding.art.load(url) {
                        usePalette { palette ->
                            palette?.createRipple(false)?.run {
                                binding.artLayout.foreground = this
                            }

                            palette?.applyControlColor()
                        }
                    }
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.snackBarMessageId.collect {
                    Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * TODO: adjust light status bar
     */
    private fun Palette.applyControlColor() {
        dominantSwatch?.rgb?.run {
            binding.appbar.animateSetBackgroundColor(this)

            val luminance = ColorUtils.calculateLuminance(this)

            val color = if (luminance < 0.5) {
                // dark color image
                Color.WHITE
            } else {
                ContextCompat.getColor(
                    requireContext(),
                    R.color.deviant_art_brand_night,
                )
            }

            binding.toolbar.animateSetColor(requireContext(), color)
        }
    }

    private fun Toolbar.animateSetColor(context: Context, @ColorInt color: Int) {
        val icon = navigationIcon ?: return

        val from = currentToolbarIconColor

        ValueAnimator.ofObject(ArgbEvaluator(), from, color).apply {
            interpolator = DecelerateInterpolator()
            duration = context.resources.getInteger(
                android.R.integer.config_shortAnimTime,
            ).toLong()

            addUpdateListener { value ->

                currentToolbarIconColor = value.animatedValue as Int

                icon.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    currentToolbarIconColor,
                    BlendModeCompat.SRC_ATOP,
                )
            }
        }.start()
    }
}

private fun View.animateSetBackgroundColor(@ColorInt color: Int) {
    val from = (background as? ColorDrawable)?.color ?: Color.TRANSPARENT
    setBackgroundColor(from)

    ValueAnimator.ofObject(ArgbEvaluator(), from, color).apply {
        interpolator = DecelerateInterpolator()
        duration = context.resources.getInteger(
            android.R.integer.config_shortAnimTime,
        ).toLong()

        addUpdateListener { value ->
            setBackgroundColor(value.animatedValue as Int)
        }
    }.start()
}
