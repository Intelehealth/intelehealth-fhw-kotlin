package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_patient_attribute_master")
data class PatientAttributeTypeMaster(
    @SerializedName("name") var name: String? = null,
    @ColumnInfo("updated_at") @SerializedName("modified_date") override var updatedAt: String? = null,
) : BaseEntity(), Parcelable
