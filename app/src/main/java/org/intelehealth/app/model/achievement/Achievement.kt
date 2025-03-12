package org.intelehealth.app.model.achievement

import com.google.gson.Gson

/**
 * Created by Vaghela Mithun R. on 26-02-2025 - 16:54.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class Achievement(
    var patientAdded: Int,
    var completedVisit: Int,
    var satisfactionScore: Double,
    var timeSpent: String
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
