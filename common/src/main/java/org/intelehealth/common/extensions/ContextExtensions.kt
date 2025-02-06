package org.intelehealth.common.extensions

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.intelehealth.common.databinding.DialogCommonMessageBinding
import org.intelehealth.common.model.DialogParams
import org.intelehealth.resource.R

/**
 * Created by Vaghela Mithun R. on 25-09-2024 - 17:07.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

fun Context.appName(): String {
    val applicationInfo = applicationInfo
    val stringId = applicationInfo.labelRes
    return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString()
    else getString(stringId)
}

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

fun Context.buildCustomDialog(binding: ViewDataBinding) = MaterialAlertDialogBuilder(this).apply {
    setView(binding.root)
}.create().apply {
    removeBackground()
}

fun Context.showOkDialog(dialogParams: DialogParams): AlertDialog = MaterialAlertDialogBuilder(this).apply {
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

fun Context.showRetryDialogOnWentWrong(onRetry: () -> Unit, onCancel: () -> Unit) {

    DialogParams(icon = R.drawable.ic_dialog_alert,
        title = R.string.title_error,
        message = R.string.content_something_went_wrong,
        positiveLbl = R.string.action_retry,
        negativeLbl = R.string.action_cancel,
        onPositiveClick = { onRetry.invoke() },
        onNegativeClick = { onCancel.invoke() }).apply {
        showCustomDialog(this)
    }
}

fun Context.showNetworkFailureDialog(onRetry: () -> Unit) {
    DialogParams(icon = R.drawable.ic_dialog_alert,
        title = R.string.dialog_title_network_failure,
        message = R.string.content_no_network,
        positiveLbl = R.string.action_retry,
        negativeLbl = 0,
        onPositiveClick = { onRetry.invoke() }).apply {
        showCustomDialog(this)
    }
}
