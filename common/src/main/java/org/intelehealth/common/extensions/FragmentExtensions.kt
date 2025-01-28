package org.intelehealth.common.extensions

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import org.intelehealth.common.model.DialogParams
import org.intelehealth.resource.R
import java.util.Locale

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
    if (!language.equals("", ignoreCase = true)) {
        Locale(language).apply {
            Locale.setDefault(this)
            val configuration: Configuration = resources.configuration
            val displayMetrics: DisplayMetrics = resources.displayMetrics
            configuration.setLocale(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(this))
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                requireActivity().createConfigurationContext(configuration)
            } else {
                resources.updateConfiguration(configuration, displayMetrics)
            }
        }
    }
    return requireContext()
}

