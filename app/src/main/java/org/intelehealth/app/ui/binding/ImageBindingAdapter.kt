package org.intelehealth.app.ui.binding

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.signature.ObjectKey
import com.github.ajalt.timberkt.Timber
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.intelehealth.app.R
import org.intelehealth.app.model.IntroContent
import org.intelehealth.app.utility.PERSON_IMAGE_BASE_PATH
import org.intelehealth.common.helper.PreferenceHelper
import org.intelehealth.common.utility.PreferenceUtils
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 25-04-2024 - 11:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

const val PROFILE_PIC_SIZE_MULTIPLIER = 0.30f

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
            .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
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
        val requestBuilder = Glide.with(imageView.context).asDrawable()
            .sizeMultiplier(PROFILE_PIC_SIZE_MULTIPLIER)
        Glide.with(imageView.context).load(File(url)).thumbnail(requestBuilder).centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView)
    }
}

@BindingAdapter("personImageId")
fun bindPersonImage(imageView: ImageView?, personId: String?) {
    if (imageView != null && !personId.isNullOrEmpty()) {
        val path = PERSON_IMAGE_BASE_PATH + personId
        Timber.d { "Person image path => $path" }
        loadImageWithAuth(imageView, path)
    }
}

/**
 * A [BindingAdapter] function that loads an image from a URL into an
 * [ImageView] using Glide.
 *
 * This function is designed to be used in data binding layouts to simplify
 * loading images from URLs into [ImageView]s. It uses Glide for efficient
 * image loading and caching.
 *
 * @param imageView The [ImageView] to load the image into.
 * @param url The URL of the image to load. If `null` or empty, no image is loaded.
 */
@BindingAdapter("imgUrl")
fun bindImage(imageView: ImageView?, url: String?) {
    if (imageView != null && !url.isNullOrEmpty()) {
        Glide.with(imageView.context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }
}

@BindingAdapter("imgResource")
fun bindImageResource(imageView: ImageView?, @DrawableRes resourceId: Int?) {
    if (imageView != null && resourceId != null) {
        imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, resourceId))
//        Glide.with(imageView.context)
//            .load(resourceId)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .into(imageView)
    }
}

fun loadImageWithAuth(imageView: ImageView, url: String) {
    PreferenceUtils(PreferenceHelper(imageView.context.applicationContext)).apply {
        val glideUrl = GlideUrl(url, LazyHeaders.Builder().addHeader("Authorization", basicAuthToken).build())

        val requestBuilder = Glide.with(imageView.context).asDrawable()
            .sizeMultiplier(PROFILE_PIC_SIZE_MULTIPLIER)

        Glide.with(imageView.context)
            .load(glideUrl)
            .thumbnail(requestBuilder)
            .centerCrop()
            .placeholder(org.intelehealth.resource.R.drawable.avatar1)
            .error(org.intelehealth.resource.R.drawable.avatar1)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .signature(ObjectKey(5L))
            .into(imageView)
    }
}
