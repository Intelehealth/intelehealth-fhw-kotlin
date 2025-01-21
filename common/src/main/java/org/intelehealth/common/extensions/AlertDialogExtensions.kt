package org.intelehealth.common.extensions

import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import org.intelehealth.resource.R

/**
 * Created by Vaghela Mithun R. on 01-01-2025 - 20:47.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
fun AlertDialog.changeButtonTheme() {
    // Getting the view elements
    window?.findViewById<TextView>(android.R.id.message)?.typeface =
        ResourcesCompat.getFont(context, R.font.lato_regular)
    window?.findViewById<TextView>(androidx.appcompat.R.id.alertTitle)
        ?.setTypeface(ResourcesCompat.getFont(context, R.font.lato_bold))
    window?.findViewById<TextView>(android.R.id.button1)?.typeface = ResourcesCompat.getFont(context, R.font.lato_bold)
    window?.findViewById<TextView>(android.R.id.button2)?.typeface = ResourcesCompat.getFont(context, R.font.lato_bold)
}

fun AlertDialog.removeBackground() {
    // set transparent background
    window?.setBackgroundDrawableResource(android.R.color.transparent)
    // add blur flag
    window?.setFlags(
        android.view.WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
        android.view.WindowManager.LayoutParams.FLAG_BLUR_BEHIND
    )
    // set wrap content
    window?.setLayout(
        android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT
    )
}