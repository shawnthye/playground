package feature.playground.deviant.ui.track

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import app.playground.store.database.entities.Deviation
import app.playground.store.database.entities.TrackWithDeviation
import coil.load
import coil.request.repeatCount
import feature.playground.deviant.R
import feature.playground.deviant.databinding.DeviationItemBinding
import feature.playground.deviant.ui.selectableItemBackground
import feature.playground.deviant.widget.PaletteExtensions.findRippleColor
import feature.playground.deviant.widget.createRipple
import feature.playground.deviant.widget.usePaletteTransition

class TrackAdapter(
    private val onItemClickListener: OnItemClickListener,
) : PagingDataAdapter<TrackWithDeviation, DeviationViewHolder>(TrackAdapterItemCallback) {

    interface OnPaletteListener {
        fun onPaletteReady(deviation: Deviation, palette: Palette)
    }

    interface OnItemClickListener {
        fun onItemClicked(id: String)
    }

    override fun onBindViewHolder(holder: DeviationViewHolder, position: Int) {
        holder.bind(getItem(position)?.deviation)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.deviation_item
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DeviationViewHolder = DeviationViewHolder(parent, onItemClickListener)
}

fun TrackAdapter.withFooter(
    footer: LoadStateAdapter<*>,
): ConcatAdapter {
    addLoadStateListener { loadStates ->
        footer.loadState = loadStates.append
    }
    return ConcatAdapter(
        ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build(),
        this,
        footer,
    )
}

class DeviationViewHolder(
    parent: ViewGroup,
    onItemClickListener: TrackAdapter.OnItemClickListener,
    private val binding: DeviationItemBinding = DeviationItemBinding.inflate(
        LayoutInflater.from(parent.context), parent, false,
    ),
) : RecyclerView.ViewHolder(binding.root), TrackAdapter.OnPaletteListener {

    init {
        binding.onItemClickListener = onItemClickListener
        binding.onPaletteListener = this
    }

    fun bind(deviation: Deviation?) {
        binding.deviation = deviation
        binding.executePendingBindings()
    }

    override fun onPaletteReady(deviation: Deviation, palette: Palette) {
        val foreground = palette
            .findRippleColor()
            ?.createRipple(false)
            ?: binding.imageLayout.context.selectableItemBackground
        binding.imageLayout.foreground = foreground
    }
}

@BindingAdapter("trackImageDeviation", "trackImagePlaceholder", "trackImageOnPaletteListener")
fun deviationImage(
    image: ImageView,
    deviation: Deviation?,
    placeholder: Drawable?,
    onPaletteListener: TrackAdapter.OnPaletteListener,
) {
    onPaletteListener.let { }
    if (deviation != null) {
        image.load(uri = deviation.imageUrl) {
            repeatCount(0)
            placeholder(drawable = placeholder)
            if (deviation.rippleColor == 0) {
                usePaletteTransition { palette ->
                    palette?.also { onPaletteListener.onPaletteReady(deviation, it) }
                }
            }
        }
    } else {
        image.load(drawable = placeholder) {
            crossfade(false)
        }
    }
}
