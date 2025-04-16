package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_visit_attribute")
data class VisitAttribute(
    @ColumnInfo("visit_uuid") @SerializedName("visit_uuid") var visitUuid: String? = null,

    var value: String? = null,

    @SerializedName("name") @Expose var name: String = "",

    @SerializedName("retired") @Expose var retired: Int = 0,

    @ColumnInfo("visit_attribute_type_uuid")
    @SerializedName("visit_attribute_type_uuid") var visitAttributeTypeUuid: String? = null,
) : BaseEntity(), Parcelable
