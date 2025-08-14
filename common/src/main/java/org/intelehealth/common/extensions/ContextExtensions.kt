package org.intelehealth.common.extensions

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.intelehealth.common.databinding.DialogCommonMessageBinding
import org.intelehealth.common.model.DialogParams
import org.intelehealth.common.utility.ArrayAdapterUtils
import org.intelehealth.resource.R
import java.util.Locale

/**
 * Created by Vaghela Mithun R. on 25-09-2024 - 17:07.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

const val ENGLISH = "en"

/**
 * Extension function to get the application name from the context.
 * It retrieves the application info and checks if a string resource ID is available.
 * If not, it returns the non-localized label as a string.
 *
 * @receiver Context The context from which the application name is retrieved.
 * @return String The application name.
 */
fun Context.appName(): String {
    val applicationInfo = applicationInfo
    val stringId = applicationInfo.labelRes
    return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString()
    else getString(stringId)
}

/**
 * Extension function to get the application version from the context.
 * It retrieves the package info and returns the version name and version code.
 *
 * @receiver Context The context from which the application version is retrieved.
 * @return String The application version in the format "versionName(versionCode)".
 */
fun Context.appVersion(): String {
    packageManager.getPackageInfo(packageName, 0).apply {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            "${versionName}(${longVersionCode})"
        } else {
            @Suppress("DEPRECATION")
            "${versionName}(${versionCode})"
        }
    }
}

//fun Context.showOkDialog(title: String, message: String, okLabel: String, onOkClick: () -> Unit) {
//    val alertDialog = MaterialAlertDialogBuilder(this)
//    alertDialog.setTitle(title)
//    alertDialog.setMessage(message)
//    alertDialog.setPositiveButton(okLabel) { dialog, which ->
//        dialog.dismiss()
//        onOkClick.invoke()
//    }
//    val dialog = alertDialog.show()
//    val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//    positiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
//    dialog.changeButtonTheme()
//}

/**
 * Extension function to show a custom dialog with the provided parameters.
 * It inflates a custom layout, sets the parameters, and handles button clicks.
 *
 * @receiver Context The context in which the dialog is shown.
 * @param dialogParams The parameters for the dialog, including title, message, and button labels.
 */
fun Context.showCustomDialog(dialogParams: DialogParams) {
    val inflater = LayoutInflater.from(this)
    val binding = DialogCommonMessageBinding.inflate(inflater)
    binding.params = dialogParams
//    binding.ivDialogIcon.setImageResource(dialogParams.icon)
//    binding.tvDialogTitle.text = resources.getText(dialogParams.title)
//    binding.tvDialogMessage.text = resources.getText(dialogParams.message)
//    binding.btnDialogYes.text = resources.getText(dialogParams.positiveLbl)
//    binding.btnDialogNo.text = resources.getText(dialogParams.negativeLbl)

    buildCustomDialog(binding).apply {
        binding.btnDialogYes.setOnClickListener {
            dismiss()
            dialogParams.onPositiveClick.invoke()
        }
        binding.btnDialogNo.setOnClickListener {
            dismiss()
            dialogParams.onNegativeClick.invoke()
        }
        resources.getDimensionPixelSize(R.dimen.dialog_width).let {
            window?.setLayout(it, WindowManager.LayoutParams.WRAP_CONTENT)
        }
    }.show()
}

/**
 * Extension function to build a custom dialog with the provided binding.
 * It sets the view of the dialog to the root of the binding.
 *
 * @receiver Context The context in which the dialog is built.
 * @param binding The ViewDataBinding instance for the custom layout.
 * @return AlertDialog The created AlertDialog instance.
 */
fun Context.buildCustomDialog(binding: ViewDataBinding) = MaterialAlertDialogBuilder(this).apply {
    setView(binding.root)
}.create().apply {
    removeBackground()
}

/**
 * Extension function to show an OK dialog with the provided parameters.
 * It sets the title, message, and button labels, and handles button clicks.
 *
 * @receiver Context The context in which the dialog is shown.
 * @param dialogParams The parameters for the dialog, including title, message, and button labels.
 * @return AlertDialog The created AlertDialog instance.
 */
fun Context.showOkDialog(dialogParams: DialogParams): AlertDialog =
    MaterialAlertDialogBuilder(this).apply {
        setTitle(dialogParams.title)
        setMessage(dialogParams.message)
        setPositiveButton(dialogParams.positiveLbl) { dialog, _ ->
            dialog.dismiss()
            dialogParams.onPositiveClick.invoke()
        }
        if (dialogParams.negativeLbl != 0) {
            setNegativeButton(dialogParams.negativeLbl) { dialog, _ ->
                dialog.dismiss()
                dialogParams.onNegativeClick.invoke()
            }
        }
    }.show()

/**
 * Extension function to show a retry dialog when something goes wrong.
 * It sets the title, message, and button labels, and handles button clicks.
 *
 * @receiver Context The context in which the dialog is shown.
 * @param onRetry The lambda function to be invoked when the retry button is clicked.
 * @param onCancel The lambda function to be invoked when the cancel button is clicked.
 */
fun Context.showRetryDialogOnWentWrong(onRetry: () -> Unit, onCancel: () -> Unit) {

    DialogParams(
        icon = R.drawable.ic_dialog_alert,
        title = R.string.title_error,
        message = R.string.content_something_went_wrong,
        positiveLbl = R.string.action_retry,
        negativeLbl = R.string.action_cancel,
        onPositiveClick = { onRetry.invoke() },
        onNegativeClick = { onCancel.invoke() }).apply {
        showCustomDialog(this)
    }
}

/**
 * Extension function to show a network failure dialog.
 * It sets the title, message, and button labels, and handles button clicks.
 *
 * @receiver Context The context in which the dialog is shown.
 * @param onRetry The lambda function to be invoked when the retry button is clicked.
 */
fun Context.showNetworkFailureDialog(onRetry: () -> Unit) {
    DialogParams(
        icon = R.drawable.ic_dialog_alert,
        title = R.string.dialog_title_network_failure,
        message = R.string.content_no_network,
        positiveLbl = R.string.action_retry,
        negativeLbl = 0,
        onPositiveClick = { onRetry.invoke() }).apply {
        showCustomDialog(this)
    }
}

fun Context.getSpinnerArrayAdapter(
    @ArrayRes arrayResId: Int
) = ArrayAdapterUtils.getSpinnerArrayAdapter(this, arrayResId)

fun <T> Context.getSpinnerItemAdapter(
    list: List<T>
) = ArrayAdapterUtils.getSpinnerItemAdapter(this, list)

fun Context.clearData() {
    val runtime = Runtime.getRuntime()
    runtime.exec("pm clear ${applicationContext.packageName}")
}

fun Context.getLocalResource(locale: String = ENGLISH): Resources {
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(Locale(locale))
    return createConfigurationContext(configuration).resources
}

fun Context.getLocalString(locale: String = ENGLISH, @StringRes resId: Int): String {
    return getLocalResource(locale).getString(resId)
}

fun Context.getLocalValueFromArray(
    dbString: String, @ArrayRes arrayResId: Int
): String {
    return if (Locale.getDefault().language == ENGLISH) {
        dbString
    } else {
        val array = resources.getStringArray(arrayResId)
        val index = getLocalResource(ENGLISH).getStringArray(arrayResId).indexOf(dbString)
        if (index >= 0) array[index] else ""
    }
}
