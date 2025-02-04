package org.intelehealth.app.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.intelehealth.app.model.IntroContent
import java.io.File

/**
 * Created by Vaghela Mithun R. on 25-04-2024 - 11:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * A [BindingAdapter] function that loads an image into an [ImageView] based on the
 * provided [IntroContent].
 *
 * This function is designed to be used in data binding layouts to simplify the
 * process of loading images, whether they are from a local resource or a URL.
 * It uses Glide for efficient image loading and caching.
 *
 * @param imageView The [ImageView] to load the image into.
 * @param content The [IntroContent] object containing the image information.
 *                If `isResourceImage` is true, `resId` is used; otherwise, `imageUrl` is used.
 */
@BindingAdapter("content")
fun bindContentImage(imageView: ImageView?, content: IntroContent?) {
    if (imageView != null && content != null) {
        Glide.with(imageView.context).load(if (content.isResourceImage) content.resId else content.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
}

/**
 * A [BindingAdapter] function that loads a profile image from a local file path into an [ImageView].
 *
 * This function is designed to be used in data binding layouts to simplify the
 * process of loading profile images from local storage. It uses Glide for
 * efficient image loading, thumbnail generation, and caching.
 *
 * @param imageView The [ImageView] to load the profile image into.
 * @param url The local file path (as a [String]) to the profile image.
 *            If the URL is null or empty, no image is loaded.
 */
@BindingAdapter("profileUrl")
fun bindProfileImage(imageView: ImageView?, url: String?) {
    if (imageView != null && !url.isNullOrEmpty()) {
        val requestBuilder = Glide.with(imageView.context).asDrawable().sizeMultiplier(0.25f)
        Glide.with(imageView.context).load(File(url)).thumbnail(requestBuilder).centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView)
    }
}