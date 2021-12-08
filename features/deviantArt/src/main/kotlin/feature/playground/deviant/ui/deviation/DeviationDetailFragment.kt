package feature.playground.deviant.ui.deviation

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.ColorUtils
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import app.playground.store.database.entities.Deviation
import coil.load
import com.google.android.material.snackbar.Snackbar
import core.playground.ui.string
import dagger.hilt.android.AndroidEntryPoint
import feature.playground.deviant.R
import feature.playground.deviant.databinding.DeviationDetailBinding
import feature.playground.deviant.ui.DeviantArtNavigationFragment
import feature.playground.deviant.widget.PaletteExtensions.createRipple
import feature.playground.deviant.widget.onCreateViewBinding
import feature.playground.deviant.widget.usePaletteTransition
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeviationDetailFragment : DeviantArtNavigationFragment() {

    private val model: DeviationDetailViewModel by viewModels()
    private lateinit var binding: DeviationDetailBinding
    private val animDurationMs by lazy {
        requireContext().resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    private var currentToolbarIconColor: Int = Color.TRANSPARENT

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
            currentToolbarIconColor = value.data
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.snackBarMessageId.collect {
                    Snackbar.make(view, it.string(requireContext()), Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.action.collect {
                    when (it) {
                        is DeviationDetailAction.ViewAuthor -> {
                            AlertDialog.Builder(requireContext())
                                .setMessage("Action View author id: ${it.id}")
                                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                                .setCancelable(false)
                                .show()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            model.deviation.mapNotNull { it?.imageUrl }
                /**
                 * doesn't need to re-load the image if url doesn't change
                 * we first get from database
                 */
                .distinctUntilChanged()
                .collect { url ->
                    binding.art.load(url) {
                        usePaletteTransition(durationMillis = animDurationMs) { palette ->
                            palette?.createRipple(false)?.run {
                                binding.artLayout.foreground = this
                            }

                            binding.applyControlsColor(palette)
                        }
                    }
                }
        }
    }

    /**
     * TODO: adjust light status bar
     */
    private fun DeviationDetailBinding.applyControlsColor(palette: Palette?) {
        val color = palette?.dominantSwatch?.rgb ?: return

        appbar.animateSetBackgroundColor(color, animDurationMs)

        val iconColor = if (ColorUtils.calculateLuminance(color) < 0.5) {
            // dark color image
            Color.WHITE
        } else {
            ContextCompat.getColor(
                root.context,
                R.color.deviant_art_brand_night,
            )
        }

        toolbar.animateSetColor(iconColor)
    }

    private fun Toolbar.animateSetColor(@ColorInt color: Int) {
        val icon = navigationIcon ?: return

        val from = currentToolbarIconColor

        ValueAnimator.ofObject(ArgbEvaluator(), from, color).apply {
            interpolator = DecelerateInterpolator()
            duration = animDurationMs.toLong()

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

private fun View.animateSetBackgroundColor(@ColorInt color: Int, duration: Int) {
    val from = (background as? ColorDrawable)?.color ?: Color.TRANSPARENT
    setBackgroundColor(from)

    ValueAnimator.ofObject(ArgbEvaluator(), from, color)
        .setDuration(duration.toLong())
        .apply {
            interpolator = DecelerateInterpolator()
            addUpdateListener { value ->
                setBackgroundColor(value.animatedValue as Int)
            }
        }.start()
}

@BindingAdapter(value = ["deviation", "listener"])
internal fun deviationDetail(
    recyclerView: RecyclerView,
    deviation: Deviation?,
    listener: DeviationDetailActionListener?,
) {
    deviation ?: return
    listener ?: throw IllegalArgumentException("Missing DeviationDetailActionListener")

    if (recyclerView.adapter == null) {
        recyclerView.adapter = DeviationDetailAdapter(listener)
    }

    (recyclerView.adapter as DeviationDetailAdapter).apply {
        submitList(deviation.toUiModel())
    }
}
