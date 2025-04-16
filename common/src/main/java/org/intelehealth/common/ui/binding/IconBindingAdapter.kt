package org.intelehealth.common.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter

/**
 * Created by Vaghela Mithun R. on 03-01-2025 - 16:38.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

@BindingAdapter("android:icon")
fun bindResourceIcon(imageView: ImageView, iconResId: Int) {
    imageView.setImageResource(iconResId)
}