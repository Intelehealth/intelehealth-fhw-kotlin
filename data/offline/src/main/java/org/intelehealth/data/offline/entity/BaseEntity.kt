package org.intelehealth.data.offline.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Vaghela Mithun R. on 14-04-2025 - 18:37.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Entity
open class BaseEntity(
    @PrimaryKey
    open var uuid: String = "",
    @ColumnInfo("created_at")
    open var createdAt: String? = null,
    @ColumnInfo("updated_at")
    open var updatedAt: String? = null,
    open var synced: Boolean = false,
    var voided: Int = 0,
)
