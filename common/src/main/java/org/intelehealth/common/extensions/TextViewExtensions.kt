package org.intelehealth.common.extensions

import android.annotation.SuppressLint
import android.widget.TextView
import com.github.ajalt.timberkt.Timber

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
    Timber.d { "spanString : $spanString" }
    spanString.setSpan(clickableSpan, startingPosition, endingPosition, 0)
    this.text = spanString
}

@SuppressLint("ClickableViewAccessibility")
fun TextView.setCompoundDrawableClick(gravity: Int, onClick: (TextView) -> Unit) {
    this.setOnTouchListener { v, event ->
        if (event.action == android.view.MotionEvent.ACTION_UP) {
            val drawableEnd = this.compoundDrawablesRelative[2]
            if (drawableEnd != null) {
                val drawableBounds = drawableEnd.bounds
                val x = event.x.toInt()
                val y = event.y.toInt()
                val drawableStartX = this.width - this.paddingEnd - drawableBounds.width()
                val drawableEndX = this.width - this.paddingEnd
                val isDrawableClicked = when (gravity) {
                    android.view.Gravity.END -> x in drawableStartX..drawableEndX
                    android.view.Gravity.START -> x in this.paddingStart..(this.paddingStart + drawableBounds.width())
                    else -> false
                }
                if (isDrawableClicked && y >= this.paddingTop && y <= this.height - this.paddingBottom) {
                    onClick(this)
                    return@setOnTouchListener true
                }
            }
        }
        false
    }
}