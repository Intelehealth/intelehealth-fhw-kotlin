package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_encounter")
open class Encounter(
    @ColumnInfo("visituuid") @SerializedName("visituuid") var visitUuid: String? = null,
    @ColumnInfo("encounter_type_uuid") @SerializedName("encounter_type_uuid") var encounterTypeUuid: String? = null,
    @ColumnInfo("encounter_time") @SerializedName("encounter_time") var encounterTime: String? = null,
    @ColumnInfo("provider_uuid") @SerializedName("provider_uuid") var providerUuid: String? = null,
    @ColumnInfo("updated_at") @SerializedName("modified_date") override var updatedAt: String? = null,
    @SerializedName("syncd")
    override var synced: Boolean = false
//    @ColumnInfo("privacynotice_value") @SerializedName("privacynotice_value") var privacyNoticeValue: String? = null
) : BaseEntity(), Parcelable
