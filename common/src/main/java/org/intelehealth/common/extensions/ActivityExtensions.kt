package org.intelehealth.common.extensions

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.github.ajalt.timberkt.Timber
import com.google.android.material.snackbar.Snackbar
import org.intelehealth.common.model.DialogParams
import java.util.Locale

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

fun ComponentActivity.showSnackBar(message: String) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
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

