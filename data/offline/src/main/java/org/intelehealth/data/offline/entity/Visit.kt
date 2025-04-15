package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_visit")
data class Visit(
    @ColumnInfo("patientuuid") @SerializedName("patientuuid") var patientUuid: String? = null,
    @ColumnInfo("visit_type_uuid") @SerializedName("visit_type_uuid") var visitTypeUuid: String? = null,
    @ColumnInfo("startdate") @SerializedName("startdate") var startDate: String? = null,
    @ColumnInfo("enddate") @SerializedName("enddate") var endDate: String? = null,
    @ColumnInfo("locationuuid") @SerializedName("locationuuid") var locationUuid: String? = null,
    @ColumnInfo("creator_uuid") @SerializedName("creator_uuid") var creatorUuid: String? = null,
    @SerializedName("syncd") override var synced: Boolean = false,
    @ColumnInfo("updated_at") @SerializedName("modified_date") override var updatedAt: String? = null,
    @ColumnInfo("isdownloaded") @SerializedName("isdownloaded") var downloaded: Boolean = false,
    @ColumnInfo("issubmitted") @SerializedName("issubmitted") var submitted: Int = 0
) : BaseEntity(), Parcelable {
    @Ignore
    @IgnoredOnParcel
    @SerializedName("attributes")
    var visitAttrs: List<VisitAttribute>? = null

    override fun toString(): String = Gson().toJson(this)
}
