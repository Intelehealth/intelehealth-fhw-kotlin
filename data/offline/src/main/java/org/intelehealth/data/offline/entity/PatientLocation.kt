package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Not in use
 */
@Parcelize
@Entity(tableName = "tbl_location")
data class PatientLocation(
    @SerializedName("name") var name: String,
    @PrimaryKey
    @ColumnInfo("locationuuid") @SerializedName("locationuuid") override var uuid: String,
    @SerializedName("retired") val retired: Int? = null,
    @ColumnInfo("updated_at") @SerializedName("modified_date") override var updatedAt: String? = null,
    @SerializedName("syncd")
    override var synced: Boolean = false
) : BaseEntity(), Parcelable
