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
    @PrimaryKey
    @SerializedName("uuid") var uuid: String,
    @SerializedName("name") var name: String? = null,
    @ColumnInfo("modified_date") @SerializedName("modified_date") var modifiedDate: String? = null,
    @SerializedName("voided") var voided: Int = 0,
    @SerializedName("syncd") var synced: Boolean = false
) : Parcelable