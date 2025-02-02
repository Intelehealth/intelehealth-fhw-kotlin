package org.intelehealth.common.extensions

import android.view.View
import androidx.annotation.StringRes
import org.intelehealth.common.ui.custom.TooltipWindow

/**
 * Created by Vaghela Mithun R. on 15-01-2025 - 18:03.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.showTooltip(@StringRes resId: Int) {
    val message = context.getString(resId)
    TooltipWindow(context).showTooltip(this, message)
}

fun View.showTooltipOnClick(@StringRes resId: Int) {
    val message = context.getString(resId)
    setOnClickListener {
        TooltipWindow(context).showTooltip(this, message)
    }
}

fun View.previousFocus() {
    val previousView = focusSearch(View.FOCUS_LEFT)
    previousView?.requestFocus()
}
