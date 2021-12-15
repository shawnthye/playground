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
import feature.playground.deviant.widget.createRipple
import feature.playground.deviant.widget.usePaletteTransition
import timber.log.Timber

class TrackAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val onPaletteListener: OnPaletteListener,
) : PagingDataAdapter<TrackWithDeviation, DeviationViewHolder>(TrackAdapterItemCallback) {

    interface OnPaletteListener {
        fun onPaletteReady(deviation: Deviation, palette: Palette)
    }

    interface OnItemClickListener {
        fun onItemClicked(id: String)
    }

    override fun onBindViewHolder(holder: DeviationViewHolder, position: Int) {
        holder.bind(getItem(position)!!.relation)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.deviation_item
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DeviationViewHolder = DeviationViewHolder(parent, onItemClickListener, onPaletteListener)
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
    onPaletteListener: TrackAdapter.OnPaletteListener,
    private val binding: DeviationItemBinding = DeviationItemBinding.inflate(
        LayoutInflater.from(parent.context), parent, false,
    ),
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.onItemClickListener = onItemClickListener
        binding.onPaletteListener = onPaletteListener
    }

    fun bind(deviation: Deviation) {
        Timber.i("deviation.rippleColor - ${deviation.rippleColor}")
        if (0 != deviation.rippleColor) {
            binding.imageLayout.foreground = deviation.rippleColor.createRipple(false)
        } else {
            binding.imageLayout.foreground = binding.imageLayout.context.selectableItemBackground
        }

        binding.deviation = deviation
        binding.executePendingBindings()
    }
}

@BindingAdapter("trackImageDeviation", "trackImagePlaceholder", "trackImageOnPaletteListener")
fun deviationImage(
    image: ImageView,
    deviation: Deviation,
    placeholder: Drawable?,
    onPaletteListener: TrackAdapter.OnPaletteListener,
) {
    image.load(uri = deviation.imageUrl) {
        repeatCount(0)
        placeholder(drawable = placeholder)
        if (deviation.rippleColor == 0) {
            usePaletteTransition { palette ->
                palette?.also { onPaletteListener.onPaletteReady(deviation, it) }
            }
        }
    }
}
