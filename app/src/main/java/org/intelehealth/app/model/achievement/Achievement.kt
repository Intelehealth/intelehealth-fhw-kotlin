package org.intelehealth.app.model.achievement

import com.google.gson.Gson

/**
 * Created by Vaghela Mithun R. on 26-02-2025 - 16:54.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

/**
 * Represents a user's achievements or progress metrics.
 *
 * This data class holds information about a user's accomplishments,
 * such as the number of patients added, completed visits,
 * satisfaction score, and time spent.
 *
 * @property patientAdded The number of patients added by the user.
 * @property completedVisit The number of visits completed by the user.
 * @property satisfactionScore The user's satisfaction score, as a Double.
 * @property timeSpent The total time spent by the user, represented as a String.
 *   The format of this string is not enforced by the class and should be
 *   documented separately if a specific format is expected (e.g., "HH:mm:ss").
 */
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
