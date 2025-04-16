package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

/**
 * Created by Vaghela Mithun R. on 11-03-2025 - 17:39.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Parcelize
@Entity(tableName = "tbl_user_session")
data class UserSession(
    @PrimaryKey(autoGenerate = true)
    var sessionId: Int = 0,
    var userId: String,
    var startTime: String,
    var endTime: String,
    var sessionDuration: String
) : Parcelable {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
