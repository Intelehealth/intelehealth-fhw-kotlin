package org.intelehealth.common.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.intelehealth.resource.R

/**
 * Created by Vaghela Mithun R. on 04-02-2025 - 11:46.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * An abstract base activity that provides a consistent status bar color
 * and appearance across different Android versions.
 *
 * This class extends [AppCompatActivity] and handles setting the status bar
 * color to `colorPrimaryDark` and making the status bar icons (e.g., battery,
 * time) light for better visibility on dark backgrounds.
 *
 * Subclasses can inherit from this class to automatically get this status bar
 * behavior without needing to implement it in each activity.
 */
open class BaseStatusBarActivity : AppCompatActivity() {
    /**
     * Called when the activity is first created.
     *
     * This method sets the status bar color and appearance based on the
     * Android version:
     * - For Android Lollipop (API 21) and above:
     *   - Sets the status bar color to `colorPrimaryDark`.
     *   - For Android R (API 30) and above:
     *     - Sets the status bar icons to light for better visibility on dark backgrounds.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down, this Bundle contains the data it most
     *     recently supplied in [onSaveInstanceState]. Otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Set the status bar color
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                window.insetsController?.setSystemBarsAppearance(
//                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
//                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//                )
//                window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
//            } else {
//                window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
//            }
//        }
        val color = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15+
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars()).top
                view.setPadding(0, statusBarInsets, 0, 0)
                view.setBackgroundColor(color)
                insets
            }
        } else {
            // For Android 14 and below
            window.statusBarColor = color
        }
        super.onCreate(savedInstanceState)
    }
}
