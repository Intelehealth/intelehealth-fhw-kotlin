package org.intelehealth.common.ui.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.github.ajalt.timberkt.Timber
import com.google.android.material.button.MaterialButton

/**
 * Created by Vaghela Mithun R. on 03-01-2025 - 16:31.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

@BindingAdapter("android:value")
fun bindResourceString(textView: TextView, resId: Int) {
    if (resId != 0) {
        textView.text = textView.context.getString(resId)
    }
}

@BindingAdapter("android:action")
fun bindResourceString(btn: MaterialButton, resId: Int) {
    if (resId != 0) {
        btn.text = btn.context.getString(resId)
    }
}

@BindingAdapter(value = ["android:placeholder", "android:text"], requireAll = false)
fun bindStringWithPlaceholder(textView: TextView, placeholder: String?, text: String?) {
    if (placeholder.isNullOrEmpty().not() && text.isNullOrEmpty()) {
        textView.text = placeholder
        Timber.d { "Placeholder => $placeholder" }
    } else textView.text = text ?: ""
}
