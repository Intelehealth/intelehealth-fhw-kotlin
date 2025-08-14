package org.intelehealth.common.extensions

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.github.ajalt.timberkt.Timber
import com.google.android.material.snackbar.Snackbar
import org.intelehealth.common.databinding.SnackbarViewBinding
import org.intelehealth.common.model.DialogParams
import java.util.Locale
import org.intelehealth.resource.R as ResourceR
import android.view.View
import android.widget.FrameLayout
import org.intelehealth.common.utility.DateTimeResource

/**
 * Created by Vaghela Mithun R. on 15-04-2024 - 13:37.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

/**
 * Displays a short toast message.
 *
 * This extension function on [ComponentActivity] simplifies showing a toast
 * message.  It uses the application context to create the toast and displays
 * it for a short duration.
 *
 * @param message The message to display in the toast.
 */
fun ComponentActivity.showToast(message: String) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}

/**
 * Displays a short toast message using a string resource.
 *
 * This extension function on [ComponentActivity] simplifies showing a toast
 * message.  It uses the application context to create the toast and displays
 * it for a short duration.
 *
 * @param resId The resource ID of the string to display in the toast.
 */
fun ComponentActivity.showToast(@StringRes resId: Int) {
    Toast.makeText(applicationContext, getString(resId), Toast.LENGTH_SHORT).show()
}

/**
 * Displays a success-themed Snackbar with an icon.
 *
 * This extension function on [ComponentActivity] shows a [com.google.android.material.snackbar.Snackbar]
 * with a success theme, including a success icon. It uses the
 * [showSnackBarWithIcon] function (assumed to be defined elsewhere in your
 * project) to handle the actual Snackbar display.
 *
 * @param anchorView An optional [View] to anchor the Snackbar to. If `null`, the
 *   Snackbar will appear at the bottom of the screen.
 * @param message The string resource ID of the success message to display.
 */
fun ComponentActivity.showSuccessSnackBar(anchorView: View? = null, @StringRes message: Int) {
    showSnackBarWithIcon(anchorView, getString(message), ResourceR.drawable.ic_success)
}

/**
 * Displays an error-themed Snackbar with an icon.
 *
 * This extension function on [ComponentActivity] shows a [com.google.android.material.snackbar.Snackbar]
 * with an error theme, including an error icon. It uses the
 * [showSnackBarWithIcon] function (assumed to be defined elsewhere in your
 * project) to handle the actual Snackbar display.
 *
 * @param anchorView An optional [View] to anchor the Snackbar to. If `null`, the
 *   Snackbar will appear at the bottom of the screen.
 * @param message The string resource ID of the error message to display.
 */
fun ComponentActivity.showErrorSnackBar(anchorView: View? = null, @StringRes message: Int) {
    showSnackBarWithIcon(
        anchorView,
        getString(message),
        ResourceR.drawable.ic_error,
        ResourceR.color.red
    )
}

/**
 * Displays a success-themed Snackbar with an icon.
 *
 * This extension function on [ComponentActivity] shows a [com.google.android.material.snackbar.Snackbar]
 * with a success theme, including a success icon. It uses the
 * [showSnackBarWithIcon] function (assumed to be defined elsewhere in your
 * project) to handle the actual Snackbar display.
 *
 * @param anchorView An optional [View] to anchor the Snackbar to. If `null`, the
 *   Snackbar will appear at the bottom of the screen.
 * @param message The success message to display.
 */
fun ComponentActivity.showSuccessSnackBar(anchorView: View? = null, message: String) {
    showSnackBarWithIcon(anchorView, message, ResourceR.drawable.ic_success)
}

/**
 * Displays an error-themed Snackbar with an icon.
 *
 * This extension function on [ComponentActivity] shows a [com.google.android.material.snackbar.Snackbar]
 * with an error theme, including an error icon. It uses the
 * [showSnackBarWithIcon] function (assumed to be defined elsewhere in your
 * project) to handle the actual Snackbar display.
 *
 * @param anchorView An optional [View] to anchor the Snackbar to. If `null`, the
 *   Snackbar will appear at the bottom of the screen.
 * @param message The error message to display.
 */
fun ComponentActivity.showErrorSnackBar(anchorView: View? = null, message: String) {
    showSnackBarWithIcon(anchorView, message, ResourceR.drawable.ic_error, ResourceR.color.red)
}

/**
 * Displays a Snackbar indicating that the network is lost.
 *
 * This extension function on [ComponentActivity] shows a [com.google.android.material.snackbar.Snackbar]
 * with a network lost theme, including an error icon. It uses the
 * [showSnackBarWithIcon] function (assumed to be defined elsewhere in your
 * project) to handle the actual Snackbar display.
 *
 * @param anchorView An optional [View] to anchor the Snackbar to. If `null`, the
 *   Snackbar will appear at the bottom of the screen.
 * @param message The string resource ID of the network lost message to display.
 */
fun ComponentActivity.showNetworkLostSnackBar(
    anchorView: View? = null,
    @StringRes message: Int,
    action: () -> Unit
) {
    showSnackBarWithAction(anchorView, getString(message), ResourceR.color.red, action = action)
}

/**
 * Displays a Snackbar with an icon.
 *
 * This extension function on [ComponentActivity] shows a [com.google.android.material.snackbar.Snackbar]
 * with a custom icon and color. It uses the
 * [buildSnackBar] function (assumed to be defined elsewhere in your
 * project) to handle the actual Snackbar display.
 *
 * @param anchorView An optional [View] to anchor the Snackbar to. If `null`, the
 *   Snackbar will appear at the bottom of the screen.
 * @param message The message to display in the Snackbar.
 * @param iconResId The resource ID of the icon to display in the Snackbar.
 * @param colorResId The resource ID of the color to use for the Snackbar background.
 */
fun ComponentActivity.showSnackBarWithIcon(
    anchorView: View? = null,
    message: String,
    @DrawableRes iconResId: Int,
    @ColorRes colorResId: Int = ResourceR.color.white
) = buildSnackBar(anchorView, message, iconResId, colorResId).show()

/**
 * Displays a Snackbar with an action button.
 *
 * This extension function on [ComponentActivity] shows a [com.google.android.material.snackbar.Snackbar]
 * with a custom action button. It uses the
 * [buildSnackBar] function (assumed to be defined elsewhere in your
 * project) to handle the actual Snackbar display.
 *
 * @param anchorView An optional [View] to anchor the Snackbar to. If `null`, the
 *   Snackbar will appear at the bottom of the screen.
 * @param message The message to display in the Snackbar.
 * @param colorResId The resource ID of the color to use for the Snackbar background.
 * @param actionText The resource ID of the text for the action button.
 * @param action The action to perform when the action button is clicked.
 */
fun ComponentActivity.showSnackBarWithAction(
    anchorView: View? = null,
    message: String,
    @ColorRes colorResId: Int = ResourceR.color.white,
    @StringRes actionText: Int = ResourceR.string.action_retry,
    action: () -> Unit
) {
    val snackBar =
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
    snackBar.setBackgroundTint(getColor(colorResId))
    anchorView?.let { snackBar.anchorView = it }
    snackBar.setActionTextColor(getColor(ResourceR.color.white))
    snackBar.setAction(getString(actionText)) { action() }
    snackBar.show()
}

/**
 * Builds a Snackbar with a custom icon and color.
 *
 * This extension function on [ComponentActivity] creates a [com.google.android.material.snackbar.Snackbar]
 * with a custom icon and color. It uses the
 * [SnackbarViewBinding] to inflate the layout for the Snackbar.
 *
 * @param anchorView An optional [View] to anchor the Snackbar to. If `null`, the
 *   Snackbar will appear at the bottom of the screen.
 * @param message The message to display in the Snackbar.
 * @param iconResId The resource ID of the icon to display in the Snackbar.
 * @param colorResId The resource ID of the color to use for the Snackbar background.
 */
private fun ComponentActivity.buildSnackBar(
    anchorView: View? = null,
    message: String,
    @DrawableRes iconResId: Int,
    @ColorRes colorResId: Int = ResourceR.color.white
): Snackbar {
    val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
    val binding = SnackbarViewBinding.inflate(LayoutInflater.from(this))
    anchorView?.let { snackbar.anchorView = it }
    if (colorResId == ResourceR.color.red) {
        binding.tvSnackbarMessage.setTextColor(getColor(ResourceR.color.white))
    }
    binding.message = message
    if (iconResId != 0) binding.icon = iconResId else binding.ivSnackbarIcon.visibility = View.GONE
    binding.clSnackbarRoot.setBackgroundColor(getColor(colorResId))
    val snackbarLayout = snackbar.view as FrameLayout
    snackbarLayout.setPadding(0, 0, 0, 0) // Remove default padding
    snackbarLayout.addView(binding.root, 0)
    return snackbar
}

/**
 * Displays an alert dialog with the specified parameters.
 *
 * This extension function on [ComponentActivity] simplifies showing an alert
 * dialog. It uses the [showOkDialog] function (assumed to be defined elsewhere
 * in your project) to handle the actual dialog display.
 *
 * @param dialogParams The parameters for the dialog, including title, message,
 *   and button text.
 */
fun ComponentActivity.showAlertDialog(dialogParams: DialogParams) {
    showOkDialog(dialogParams)
}

/**
 * Displays a custom dialog with the specified parameters.
 *
 * This extension function on [ComponentActivity] simplifies showing a custom
 * dialog. It uses the [showCustomDialog] function (assumed to be defined
 * elsewhere in your project) to handle the actual dialog display.
 *
 * @param dialogParams The parameters for the dialog, including title, message,
 *   and button text.
 */
fun ComponentActivity.showCommonDialog(dialogParams: DialogParams) {
    showCustomDialog(dialogParams)
}

/**
 * Navigates to the next activity.
 *
 * This extension function on [ComponentActivity] simplifies starting a new
 * activity. It takes an optional lambda function to modify the intent before
 * starting the activity.
 *
 * @param clazz The class of the activity to navigate to.
 * @param finish Whether to finish the current activity after starting the new one.
 * @param onIntent An optional lambda function to modify the intent before starting
 *   the new activity.
 */
fun ComponentActivity.gotoNextActivity(
    clazz: Class<out AppCompatActivity>,
    finish: Boolean = false,
    onIntent: ((Intent) -> Intent)? = null
) {
    onIntent?.let {
        startActivity(it(Intent(this, clazz)))
    } ?: Intent(this, clazz).also { startActivity(it) }

    if (finish) finish()
}

/**
 * Changes the language of the application.
 *
 * This extension function on [ComponentActivity] changes the application's
 * language to the specified language code. It updates the locale and
 * configuration accordingly.
 *
 * @param language The language code to set (e.g., "en", "fr").
 * @return The updated context with the new language.
 */
@Suppress("DEPRECATION")
fun ComponentActivity.changeLanguage(language: String): Context {
    if (!language.equals("", ignoreCase = true)) {
        Locale(language).apply {
            Locale.setDefault(this)
            val configuration: Configuration = resources.configuration
            val displayMetrics: DisplayMetrics = resources.displayMetrics
            configuration.setLocale(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(this))
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                createConfigurationContext(configuration)
            } else {
                resources.updateConfiguration(configuration, displayMetrics)
            }

            Timber.d { "Language changed to $language" }
            Timber.d { "Language changed to ${Locale.getDefault().language}" }

            DateTimeResource.getInstance()?.updateResource(resources)
        }
    }
    return this
}
