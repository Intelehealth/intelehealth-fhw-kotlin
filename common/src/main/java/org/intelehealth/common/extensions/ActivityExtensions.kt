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

/**
 * Created by Vaghela Mithun R. on 15-04-2024 - 13:37.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

fun ComponentActivity.showToast(message: String) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}

fun ComponentActivity.showToast(@StringRes resId: Int) {
    Toast.makeText(applicationContext, getString(resId), Toast.LENGTH_SHORT).show()
}

fun ComponentActivity.showSuccessSnackBar(anchorView: View? = null, @StringRes message: Int) {
    showSnackBarWithIcon(anchorView, getString(message), ResourceR.drawable.ic_success)
}

fun ComponentActivity.showErrorSnackBar(anchorView: View? = null, @StringRes message: Int) {
    showSnackBarWithIcon(anchorView, getString(message), ResourceR.drawable.ic_error, ResourceR.color.red)
}

fun ComponentActivity.showSuccessSnackBar(anchorView: View? = null, message: String) {
    showSnackBarWithIcon(anchorView, message, ResourceR.drawable.ic_success)
}

fun ComponentActivity.showErrorSnackBar(anchorView: View? = null, message: String) {
    showSnackBarWithIcon(anchorView, message, ResourceR.drawable.ic_error, ResourceR.color.red)
}

fun ComponentActivity.showSnackBarWithIcon(
    anchorView: View? = null,
    message: String,
    @DrawableRes iconResId: Int,
    @ColorRes colorResId: Int = ResourceR.color.white
) {
    val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
    val binding = SnackbarViewBinding.inflate(LayoutInflater.from(this))
    anchorView?.let { snackbar.anchorView = it }
    if (colorResId == ResourceR.color.red) {
        binding.tvSnackbarMessage.setTextColor(getColor(ResourceR.color.white))
    }
    binding.message = message
    binding.icon = iconResId
    binding.clSnackbarRoot.setBackgroundColor(getColor(colorResId))
    val snackbarLayout = snackbar.view as FrameLayout
    snackbarLayout.setPadding(0, 0, 0, 0) // Remove default padding
    snackbarLayout.addView(binding.root, 0)
    snackbar.show()
}

fun ComponentActivity.showAlertDialog(dialogParams: DialogParams) {
    showOkDialog(dialogParams)
}

fun ComponentActivity.showCommonDialog(dialogParams: DialogParams) {
    showCustomDialog(dialogParams)
}

fun ComponentActivity.gotoNextActivity(
    clazz: Class<out AppCompatActivity>, finish: Boolean = false, onIntent: ((Intent) -> Intent)? = null
) {
    onIntent?.let {
        startActivity(it(Intent(this, clazz)))
    } ?: Intent(this, clazz).also { startActivity(it) }

    if (finish) finish()
}

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
        }
    }
    return this
}

