package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import org.intelehealth.common.model.RecentItem
import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.common.utility.DateTimeUtils.LAST_SYNC_DB_FORMAT

/**
 * Created by Vaghela Mithun R. on 01-07-2025 - 13:25.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Parcelize
@Entity(tableName = "tbl_recent_history")
data class RecentHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var tag: String,
    var action: String,
    var value: String,
    @ColumnInfo("created_at")
    var createdAt: String,
    @ColumnInfo("updated_at")
    var updatedAt: String
) : Parcelable, RecentItem() {

    override fun recentItemValue(): String = value

    enum class Tag {
        PATIENT,
        PRESCRIPTION_RECEIVED,
        PRESCRIPTION_PENDING,
        PROTOCOL
    }

    enum class Action {
        VIEW,
        ADD,
        UPDATE,
        DELETE,
        SEARCH
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun create(
            tag: Tag,
            action: Action,
            value: String = ""
        ): RecentHistory {
            return RecentHistory(
                tag = tag.name,
                action = action.name,
                value = value,
                createdAt = DateTimeUtils.getCurrentDateInUTC(LAST_SYNC_DB_FORMAT),
                updatedAt = DateTimeUtils.getCurrentDateInUTC(LAST_SYNC_DB_FORMAT)
            )
        }
    }
}
