package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

/**
 * Created by Vaghela Mithun R. on 20-01-2025 - 19:24.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Parcelize
@Entity
data class PrescriptionStatusCount(
    // Prescription pending count
    var pending: Int = 0,
    // Prescription completed count
    var completed: Int = 0,
    // Prescription received count
    var received: Int = 0,
    // Prescription  total count
    var total: Int = 0
) : Parcelable {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
