package org.intelehealth.common.utility

import android.content.Context
import android.os.Build
import java.util.Locale

/**
 * Created by Vaghela Mithun R. on 21-05-2025 - 16:28.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
object LocalUtils {
    fun getCountryName() = Locale.getDefault().displayCountry
    fun getCountryCode() = Locale.getDefault().country

    fun getLocalCountryName(context: Context): String {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
        val country = locale.displayCountry
        if (country.isNotEmpty()) return country

        // Fallback: Try network country
        val countryCode = context.resources.configuration.locales[0].country
        if (countryCode.isNotEmpty()) {
            return Locale("", countryCode).displayCountry
        }

        // Final fallback
        return "Unknown"
    }
}
