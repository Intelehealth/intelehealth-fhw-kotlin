package org.intelehealth.common.extensions

import org.intelehealth.common.model.AgePeriod

/**
 * Created by Vaghela Mithun R. on 08-05-2025 - 17:41.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

fun Long.toPeriod(): AgePeriod {
    val currentDate = System.currentTimeMillis()
    val ageInMillis = currentDate - this
    val days = ageInMillis / (24 * 60 * 60 * 1000)
    val months = days / 30
    val years = months / 12
    val remainingDays = days % 30
    val remainingMonths = months % 12
    return AgePeriod(
        years = years.toInt(),
        months = remainingMonths.toInt(),
        days = remainingDays.toInt()
    )
}
