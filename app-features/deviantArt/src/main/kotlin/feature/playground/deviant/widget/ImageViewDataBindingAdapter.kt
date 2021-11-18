package feature.playground.deviant.widget

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.load
import timber.log.Timber

@BindingAdapter(value = ["imageUrl", "placeholder", "crossFade"], requireAll = false)
fun imageUrl(imageView: ImageView, imageUrl: String?, placeholder: Drawable?, crossFade: Boolean?) {
    imageUri(imageView, imageUrl?.toUri(), placeholder, crossFade)
}

@BindingAdapter(value = ["imageUri", "placeholder", "crossFade"], requireAll = false)
fun imageUri(imageView: ImageView, imageUri: Uri?, placeholder: Drawable?, crossFade: Boolean?) {
    when (imageUri) {
        null -> {
            Timber.d("Unsetting image url")
            imageView.load(placeholder) {
                crossfade(crossFade ?: true)
            }
        }
        else -> {
            imageView.load(imageUri) {
                placeholder(placeholder)
                crossfade(crossFade ?: true)
            }
        }
    }
}
