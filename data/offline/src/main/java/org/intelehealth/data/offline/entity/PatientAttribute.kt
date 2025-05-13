package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_patient_attribute")
open class PatientAttribute(
    @SerializedName("value") var value: String? = null,
    @ColumnInfo("person_attribute_type_uuid") @SerializedName("person_attribute_type_uuid")
    var personAttributeTypeUuid: String? = null,
    @ColumnInfo("patient_uuid") @SerializedName("patientuuid") var patientUuid: String? = null,
    @ColumnInfo("updated_at") @SerializedName("modified_date") override var updatedAt: String? = null,
) : BaseEntity(), Parcelable
