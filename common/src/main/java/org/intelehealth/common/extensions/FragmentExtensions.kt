package org.intelehealth.common.extensions

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import org.intelehealth.common.model.DialogParams

/**
 * Created by Vaghela Mithun R. on 15-04-2024 - 13:37.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

// Extension functions for Fragment to show Toast and SnackBar messages, and handle dialogs.
fun Fragment.showToast(message: String) {
    requireActivity().showToast(message)
}

/**
 * Extension function to show a toast message in a Fragment.
 * It uses the activity's context to display the toast.
 *
 * @param resId The resource ID of the string to be displayed in the toast.
 */
fun Fragment.showToast(@StringRes resId: Int) {
    requireActivity().showToast(resId)
}

/**
 * Displays a success-themed Snackbar with an icon from within a Fragment.
 *
 * This extension function on [Fragment] provides a convenient way to show a
 * success-themed [com.google.android.material.snackbar.Snackbar] with an icon.
 * It delegates the actual Snackbar display to the [showSuccessSnackBar] function
 * defined on the [androidx.activity.ComponentActivity] (assumed to be available
 * through `requireActivity()`).
 *
 * @param anchorView An optional [View] to anchor the Snackbar to. If `null`, the
 *   Snackbar will appear at the bottom of the screen.
 * @param message The string resource ID of the success message to display.
 */
fun Fragment.showSuccessSnackBar(anchorView: View? = null, @StringRes message: Int) {
    requireActivity().showSuccessSnackBar(anchorView, message)
}

/**
 * Displays an error-themed Snackbar with an icon from within a Fragment.
 *
 * This extension function on [Fragment] provides a convenient way to show an
 * error-themed [com.google.android.material.snackbar.Snackbar] with an icon. It
 * delegates the actual Snackbar display to the [showErrorSnackBar] function
 * defined on the [androidx.activity.ComponentActivity] (assumed to be available
 * through `requireActivity()`).
 *
 * @param anchorView An optional [View] to anchor the Snackbar to. If `null`, the
 *   Snackbar will appear at the bottom of the screen.
 * @param message The string resource ID of the error message to display.
 */
fun Fragment.showErrorSnackBar(anchorView: View? = null, @StringRes message: Int) {
    requireActivity().showErrorSnackBar(anchorView, message)
}

/**
 * Displays a success-themed Snackbar with an icon from within a Fragment.
 *
 * This extension function on [Fragment] provides a convenient way to show a
 * success-themed [com.google.android.material.snackbar.Snackbar] with an icon.
 * It delegates the actual Snackbar display to the [showSuccessSnackBar] function
 * defined on the [androidx.activity.ComponentActivity] (assumed to be available
 * through `requireActivity()`).
 *
 * @param anchorView An optional [View] to anchor the Snackbar to. If `null`, the
 *   Snackbar will appear at the bottom of the screen.
 * @param message The success message to display as a String.
 */
fun Fragment.showSuccessSnackBar(anchorView: View? = null, message: String) {
    requireActivity().showSuccessSnackBar(anchorView, message)
}

/**
 * Displays an error-themed Snackbar with an icon from within a Fragment.
 *
 * This extension function on [Fragment] provides a convenient way to show an
 * error-themed [com.google.android.material.snackbar.Snackbar] with an icon. It
 * delegates the actual Snackbar display to the [showErrorSnackBar] function
 * defined on the [androidx.activity.ComponentActivity] (assumed to be available
 * through `requireActivity()`).
 *
 * @param anchorView An optional [View] to anchor the Snackbar to. If `null`, the
 *   Snackbar will appear at the bottom of the screen.
 * @param message The error message to display as a String.
 */
fun Fragment.showErrorSnackBar(anchorView: View? = null, message: String) {
    requireActivity().showErrorSnackBar(anchorView, message)
}

/**
 * Displays a Snackbar indicating a loss of network connectivity.
 *
 * This extension function on [Fragment] shows a [com.google.android.material.snackbar.Snackbar]
 * to inform the user that the network connection has been lost. It delegates
 * the actual Snackbar display to the [showNetworkLostSnackBar] function defined
 * on the [androidx.activity.ComponentActivity] (assumed to be available through
 * `requireActivity()`). The Snackbar typically includes a message and an action
 * (e.g., a "Retry" or "Dismiss" button), and the provided [action] callback is
 * executed if the user interacts with the action button.
 *
 * @param anchorView An optional [View] to anchor the Snackbar to. If `null`, the
 *   Snackbar will appear at the bottom of the screen.
 * @param message The string resource ID of the message to display in the
 *   Snackbar.
 * @param action A callback function to be executed when the user interacts with
 *   the Snackbar's action button.
 */
fun Fragment.showNetworkLostSnackBar(anchorView: View? = null, @StringRes message: Int, action: () -> Unit) {
    requireActivity().showNetworkLostSnackBar(anchorView, message, action)
}

/**
 * Displays an alert dialog with customizable parameters.
 *
 * This extension function on [Fragment] shows an alert dialog with a flexible
 * configuration defined by [DialogParams]. It delegates the actual dialog
 * display to the [showOkDialog] function (note the corrected function name,
 * assuming a typo in the original code) defined on the
 * [android.content.Context] (assumed to be available through `requireContext()`).
 *
 * The [DialogParams] object is expected to encapsulate various aspects of the
 * dialog, such as its title, message, and button configurations (typically an
 * "OK" button for acknowledgment).
 *
 * @param dialogParams An instance of [DialogParams] containing the configuration
 *   for the alert dialog to be displayed.
 */
fun Fragment.showAlertDialog(dialogParams: DialogParams) {
    requireContext().showOkDialog(dialogParams)
}

/**
 * Displays a common dialog with customizable parameters.
 *
 * This extension function on [Fragment] shows a dialog with a flexible
 * configuration defined by [DialogParams]. It delegates the actual dialog
 * display to the [showCustomDialog] function (note the corrected function name,
 * assuming a typo in the original code) defined on the
 * [android.content.Context] (assumed to be available through `requireContext()`).
 *
 * The [DialogParams] object is expected to encapsulate various aspects of the
 * dialog, such as its title, message, buttons, and associated actions.
 *
 * @param dialogParams An instance of [DialogParams] containing the configuration
 *   for the dialog to be displayed.
 */
fun Fragment.showCommonDialog(dialogParams: DialogParams) {
    requireContext().showCustomDialog(dialogParams)
}

/**
 * Displays a retry dialog when an operation goes wrong.
 *
 * This extension function on [Fragment] shows a dialog to inform the user that
 * an operation has failed and offers a retry option. It delegates the actual
 * dialog display to the [showRetryDialogOnWentWrong] function defined on the
 * [android.content.Context] (assumed to be available through `requireContext()`).
 * The dialog typically includes retry and cancel options, and the provided
 * [onRetry] and [onCancel] callbacks are executed accordingly.
 *
 * @param onRetry A callback function to be executed when the user chooses to
 *   retry the operation.
 * @param onCancel A callback function to be executed when the user chooses to
 *   cancel or dismiss the dialog.
 */
fun Fragment.showRetryDialogOnWentWrong(onRetry: () -> Unit, onCancel: () -> Unit) {
    requireContext().showRetryDialogOnWentWrong(onRetry, onCancel)
}

/**
 * Displays a dialog indicating a network connection failure.
 *
 * This extension function on [Fragment] shows a dialog to inform the user that
 * a network connection is unavailable. It delegates the actual dialog display
 * to the [showNetworkFailureDialog] function defined on the [android.content.Context]
 * (assumed to be available through `requireContext()`). The dialog typically
 * includes a retry option, and the provided [onRetry] callback is executed if
 * the user chooses to retry.
 *
 * @param onRetry A callback function to be executed when the user chooses to
 *   retry the network operation.
 */
fun Fragment.showNetworkFailureDialog(onRetry: () -> Unit) {
    requireContext().showNetworkFailureDialog(onRetry)
}

/**
 * Requests necessary permissions from the user.
 *
 * This extension function on [Fragment] requests required permissions from the
 * user. It delegates the actual permission request to the
 * [requestNeededPermissions] function defined on the
 * [androidx.activity.ComponentActivity] (assumed to be available through
 * `requireActivity()`).  Upon successful granting of all requested permissions,
 * the provided [onPermissionsGranted] callback is executed.
 *
 * Note: The specific permissions being requested and the logic for handling
 * permission results are assumed to be implemented within the
 * `ComponentActivity`'s `requestNeededPermissions` function.
 *
 * @param onPermissionsGranted A callback function to be executed when all
 *   necessary permissions have been granted by the user.
 */
fun Fragment.requestNeededPermissions(onPermissionsGranted: (() -> Unit)) {
    requireActivity().requestNeededPermissions { onPermissionsGranted() }
}


fun Fragment.changeLanguage(language: String): Context {
    return requireActivity().changeLanguage(language)
}

/**
 * Starts an intent to send a WhatsApp message to a specific phone number.
 *
 * This extension function on [Fragment] creates and starts an [Intent] that
 * opens WhatsApp and pre-fills a message to a given phone number.  It uses the
 * WhatsApp API for sending messages without needing direct contact access.
 *
 * Note: This function assumes that the WhatsApp application is installed on the
 * device. If WhatsApp is not installed, the system may prompt the user to
 * choose an alternative app or show an error.  You might want to add a check
 * for WhatsApp's availability and handle the case where it's not installed.
 *
 * @param phoneNumber The recipient's phone number, including the country code
 *   (e.g., "1234567890" for a US number).
 * @param message The message text to pre-fill in the WhatsApp chat.
 */
fun Fragment.startWhatsappIntent(phoneNumber: String, message: String) {
    Intent(Intent.ACTION_VIEW).apply {
        data = "https://api.whatsapp.com/send?phone=$phoneNumber&text=$message".toUri()
        startActivity(this)
    }
}

/**
 * Sets the screen title in the app bar based on the current destination's label.
 *
 * This extension function on [Fragment] retrieves the label of the current
 * navigation destination and sets it as the title of the app's action bar. It
 * uses the [findNavController] to access the current destination and updates
 * the action bar title accordingly.
 *
 * Note: This function assumes that the fragment is hosted within an
 * [AppCompatActivity] that has a support action bar.
 */
fun Fragment.applyLabelAsScreenTitle() {
    findNavController().currentDestination?.label?.let {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = it
    } ?: run {
        Timber.e { "Screen title not found in the current destination." }
    }
}
