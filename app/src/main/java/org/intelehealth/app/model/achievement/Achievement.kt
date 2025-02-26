package org.intelehealth.app.model.achievement

/**
 * Created by Vaghela Mithun R. on 26-02-2025 - 16:54.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class Achievement(
    var patientAdded: Int,
    var completedVisit: Int,
    var satisfactionScore: Double,
    var spentHrs: Int,
    var spentMins: Int
) {
    fun getSpentTime(): String {
        return "${spentHrs}h ${spentMins}m"
    }
}
