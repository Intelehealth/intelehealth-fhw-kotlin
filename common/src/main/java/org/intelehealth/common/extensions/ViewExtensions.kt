package org.intelehealth.common.extensions

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.core.view.setMargins
import org.intelehealth.common.ui.custom.TooltipWindow

/**
 * Created by Vaghela Mithun R. on 15-01-2025 - 18:03.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * Extension function to hide a [View] by setting its visibility to [View.GONE].
 *
 * This function provides a concise way to hide a view in the layout. When a view's
 * visibility is set to [View.GONE], it is not only hidden from the user, but it also
 * does not take up any space in the layout. This is in contrast to [View.INVISIBLE],
 * where the view is hidden but still occupies its original space.
 *
 * @receiver The [View] to hide.
 */
fun View.hide() {
    visibility = View.GONE
}

/**
 * Extension function to show a [View] by setting its visibility to [View.VISIBLE].
 *
 * This function provides a concise way to make a view visible in the layout. When a view's
 * visibility is set to [View.VISIBLE], it is displayed to the user and occupies its
 * designated space in the layout. This is the default visibility state for views.
 *
 * @receiver The [View] to show.
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Extension function to display a tooltip on a [View].
 *
 * This function provides a convenient way to show a tooltip with a message retrieved from
 * a string resource. It uses a [TooltipWindow] to display the tooltip.
 *
 * @receiver The [View] on which to display the tooltip.
 * @param resId The string resource ID of the message to display in the tooltip.
 */
fun View.showTooltip(@StringRes resId: Int) {
    val message = context.getString(resId)
    TooltipWindow(context).showTooltip(this, message)
}

/**
 * Extension function to display a tooltip on a [View] when it is clicked.
 *
 * This function sets an onClickListener on the view. When the view is clicked,
 * a tooltip with a message retrieved from a string resource is displayed.
 * It uses a [TooltipWindow] to display the tooltip.
 *
 * @receiver The [View] on which to display the tooltip when clicked.
 * @param resId The string resource ID of the message to display in the tooltip.
 */
fun View.showTooltipOnClick(@StringRes resId: Int) {
    val message = context.getString(resId)
    setOnClickListener {
        TooltipWindow(context).showTooltip(this, message)
    }
}

/**
 * Extension function to move the focus to the previous [View] in the layout.
 *
 * This function searches for the view to the left of the current view in the focus order
 * and requests focus on that view. This is useful for navigating between input fields,
 * such as in a form, where the user might want to move the focus back to the previous field.
 *
 * @receiver The current [View] from which to move the focus.
 */
fun View.previousFocus() {
    val previousView = focusSearch(View.FOCUS_LEFT)
    previousView?.requestFocus()
}

fun SearchView.changeToWhiteOverlayTheme() {
    maxWidth = Integer.MAX_VALUE

    val searchText = findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
    searchText.setTextColor(Color.WHITE)           // Text color
    searchText.setHintTextColor(Color.LTGRAY)


    val searchIcon = findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
    searchIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)

    val frame = findViewById<LinearLayout>(androidx.appcompat.R.id.search_edit_frame)
    val params = frame.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(0)
    frame.layoutParams = params
}
