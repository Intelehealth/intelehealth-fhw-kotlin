package org.intelehealth.common.extensions

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import org.intelehealth.common.model.DialogParams

/**
 * Created by Vaghela Mithun R. on 15-04-2024 - 13:37.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

fun Fragment.showToast(message: String) {
    requireActivity().showToast(message)
}

fun Fragment.showToast(@StringRes resId: Int) {
    requireActivity().showToast(resId)
}

fun Fragment.showSuccessSnackBar(anchorView: View? = null, @StringRes message: Int) {
    requireActivity().showSuccessSnackBar(anchorView, message)
}

fun Fragment.showErrorSnackBar(anchorView: View? = null, @StringRes message: Int) {
    requireActivity().showErrorSnackBar(anchorView, message)
}

fun Fragment.showSuccessSnackBar(anchorView: View? = null, message: String) {
    requireActivity().showSuccessSnackBar(anchorView, message)
}

fun Fragment.showErrorSnackBar(anchorView: View? = null, message: String) {
    requireActivity().showErrorSnackBar(anchorView, message)
}

fun Fragment.showAlertDialog(dialogParams: DialogParams) {
    requireContext().showOkDialog(dialogParams)
}

fun Fragment.showCommonDialog(dialogParams: DialogParams) {
    requireContext().showCustomDialog(dialogParams)
}

fun Fragment.showRetryDialogOnWentWrong(onRetry: () -> Unit, onCancel: () -> Unit) {
    requireContext().showRetryDialogOnWentWrong(onRetry, onCancel)
}

fun Fragment.showNetworkFailureDialog(onRetry: () -> Unit) {
    requireContext().showNetworkFailureDialog(onRetry)
}

fun Fragment.requestNeededPermissions(onPermissionsGranted: (() -> Unit)) {
    requireActivity().requestNeededPermissions { onPermissionsGranted() }
}

fun Fragment.changeLanguage(language: String): Context {
    return requireActivity().changeLanguage(language)
}

fun Fragment.startWhatsappIntent(phoneNumber: String, message: String) {
    Intent(Intent.ACTION_VIEW).apply {
        data = "https://api.whatsapp.com/send?phone=$phoneNumber&text=$message".toUri()
        startActivity(this)
    }
}

