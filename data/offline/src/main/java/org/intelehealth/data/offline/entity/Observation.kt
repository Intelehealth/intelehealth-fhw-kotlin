package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_obs")
data class Observation(
    @PrimaryKey
    @SerializedName("uuid") var uuid: String = "",
    @ColumnInfo("encounteruuid") @SerializedName("encounteruuid") var encounterUuid: String? = null,
    @ColumnInfo("conceptuuid") @SerializedName("conceptuuid", alternate = ["concept"]) var conceptUuid: String? = null,
    @SerializedName("value") var value: String? = null,
    @ColumnInfo("obsservermodifieddate") @SerializedName("obsServerModifiedDate") var obsServerModifiedDate: String? = null,
    @Ignore @SerializedName("obsDatetime") @Expose var obsDatetime: String = "",
    @Ignore @SerializedName("encounter") @Expose var encounter: String = "",
    @SerializedName("creator", alternate = ["person"]) var creator: String? = null,
    @SerializedName("comment") var comment: String? = null,
    @SerializedName("voided") var voided: Int? = null,
    @ColumnInfo("modified_date") @SerializedName("modified_date") var modifiedDate: String? = null,
    @ColumnInfo("created_date") @SerializedName("created_date") var createdDate: String? = null,
    @SerializedName("syncd") var synced: Boolean = false,
    @ColumnInfo("concept_set_uuid") @SerializedName("conceptsetuuid") var conceptSetUuid: String? = null,
) : Parcelable
