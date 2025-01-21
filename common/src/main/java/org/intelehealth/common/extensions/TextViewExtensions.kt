package org.intelehealth.common.extensions

import android.widget.TextView

/**
 * Created by Vaghela Mithun R. on 08-01-2025 - 16:54.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

fun TextView.enableMovementMethod() {
    this.movementMethod = android.text.method.LinkMovementMethod.getInstance()
    this.isSelected = true
    this.isClickable = true
}

fun TextView.setClickableText(clickableStr: String, onClick: () -> Unit) {
    val startingPosition = this.text.indexOf(clickableStr)
    val endingPosition = startingPosition + clickableStr.length
    setClickableText(startingPosition, endingPosition, object : android.text.style.ClickableSpan() {
        override fun onClick(widget: android.view.View) {
            onClick()
        }
    })
}

fun TextView.setClickableText(
    startingPosition: Int, endingPosition: Int, clickableSpan: android.text.style.ClickableSpan
) {
    val spanString = this.text as android.text.SpannableString
    spanString.setSpan(clickableSpan, startingPosition, endingPosition, 0)
    this.text = spanString
}