package feature.playground.deviant.ui.track

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.playground.source.of.truth.database.entities.Deviation
import app.playground.source.of.truth.database.entities.TrackWithDeviation
import coil.annotation.ExperimentalCoilApi
import coil.load
import coil.request.repeatCount
import feature.playground.deviant.R
import feature.playground.deviant.databinding.DeviationItemBinding
import feature.playground.deviant.widget.PaletteExtensions.createRipple
import feature.playground.deviant.widget.withPalette

class TrackPagingAdapter(
    private val onItemClickListener: OnItemClickListener,
) : PagingDataAdapter<TrackWithDeviation, DeviationViewHolder>(Diff) {

    interface OnItemClickListener {
        fun onItemClicked(id: String)
    }

    override fun onBindViewHolder(holder: DeviationViewHolder, position: Int) {
        getItem(position)?.relation?.run {
            holder.bind(this)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.deviation_item
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DeviationViewHolder = DeviationViewHolder(parent, onItemClickListener)
}

object Diff :
    DiffUtil.ItemCallback<TrackWithDeviation>() {
    override fun areItemsTheSame(
        oldItem: TrackWithDeviation,
        newItem: TrackWithDeviation,
    ) = oldItem.entry.id == newItem.entry.id

    override fun areContentsTheSame(
        oldItem: TrackWithDeviation,
        newItem: TrackWithDeviation,
    ) = oldItem.entry.deviationId == newItem.entry.deviationId &&
        oldItem.relation.coverUrl == newItem.relation.coverUrl
}

fun TrackPagingAdapter.withFooter(
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
    onItemClickListener: TrackPagingAdapter.OnItemClickListener,
    private val binding: DeviationItemBinding = DeviationItemBinding.inflate(
        LayoutInflater.from(parent.context), parent, false,
    ),
    private val placeholder: Drawable? = ContextCompat.getDrawable(
        parent.context,
        R.color.track_placeholder,
    ),
    private val originalForeground: Drawable? = binding.imageLayout.foreground,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.onItemClickListener = onItemClickListener
    }

    fun bind(deviation: Deviation) {
        binding.deviation = deviation
        binding.bingImage(deviation.imageUrl, placeholder, originalForeground)
        binding.executePendingBindings()
    }
}

@OptIn(ExperimentalCoilApi::class)
fun DeviationItemBinding.bingImage(
    url: String,
    placeholder: Drawable?,
    defaultForeground: Drawable?,
) {
    imageLayout.foreground = defaultForeground

    image.load(uri = url) {
        allowHardware(false)
        repeatCount(0)
        placeholder(drawable = placeholder)
        withPalette { palette ->
            imageLayout.foreground = palette?.createRipple(false) ?: defaultForeground
        }
    }
}
