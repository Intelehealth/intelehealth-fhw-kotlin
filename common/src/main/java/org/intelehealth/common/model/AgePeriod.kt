package org.intelehealth.common.model

import android.content.Context
import android.content.res.Resources
import com.github.ajalt.timberkt.Timber
import org.intelehealth.common.utility.DateTimeResource
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 08-05-2025 - 17:47.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class AgePeriod(
    val years: Int = 0,
    val months: Int = 0,
    val days: Int = 0
) {
    enum class DisplayFormat {
        YEARS,
        YEARS_MONTHS,
        YEARS_MONTHS_DAYS
    }

    override fun toString(): String {
        return "$years Years $months Months $days Days"
    }

    fun format(context: Context, format: DisplayFormat): String {
        Timber.d { "format: $this" }
        if (years == 0 && months == 0 && days == 0) {
            return "0 Years"
        }

        if (years < 0 || months < 0 || days < 0) {
            return "Invalid Age"
        }

        if (years > 100) {
            return "Invalid Age"
        }

        if (months > 12) {
            return "Invalid Age"
        }

        return when (format) {
            DisplayFormat.YEARS -> "$years ${yearResource(context)}"
            DisplayFormat.YEARS_MONTHS -> formatInYearsMonths(context)
            DisplayFormat.YEARS_MONTHS_DAYS -> formatInYearsMonthsDays(context)
        }
    }

    private fun formatInYearsMonths(context: Context): String {
        return when {
            years > 0 && months > 0 -> "$years ${yearResource(context)} $months ${monthResource(context)}"
            years == 0 && months > 0 -> "$months ${monthResource(context)}"
            years > 0 && months == 0 -> "$years ${yearResource(context)}"
            else -> "0 ${yearResource(context)}"
        }
    }

    private fun formatInYearsMonthsDays(context: Context): String {
//        Year[11]/Month[0]/Day[0]
        return when {
            years > 0 && months > 0 && days > 0 -> {
                "$years ${yearResource(context)} $months ${monthResource(context)} $days ${dayResource(context)}"
            }

            years == 0 && months > 0 && days > 0 -> "$months ${monthResource(context)} $days ${dayResource(context)}"
            years == 0 && months == 0 && days > 0 -> "$days ${dayResource(context)}"
            years > 0 && months == 0 && days > 0 -> "$years ${yearResource(context)} $days ${dayResource(context)}"
            years > 0 && months > 0 && days == 0 -> "$years ${yearResource(context)} $months ${monthResource(context)}"
            years > 0 && months == 0 && days == 0 -> "$years ${yearResource(context)}"
            else -> "0 Years"
        }
    }

    private fun yearResource(context: Context): String {
        return context.getString(ResourceR.string.lbl_years)
    }

    private fun monthResource(context: Context): String {
        return context.getString(ResourceR.string.lbl_months)
    }

    private fun dayResource(context: Context): String {
        return context.getString(ResourceR.string.lbl_days)
    }
}
