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
@Entity
data class UnSyncedEncounter(
    @ColumnInfo(name = "patientId") @SerializedName("patient")
    var patientId: String? = null,
    @ColumnInfo("locationId") @SerializedName("location")
    var locationId: String? = null
) : Encounter(), Parcelable {
    @IgnoredOnParcel
    @Ignore
    @SerializedName("obs")
    var observations: List<Observation>? = null

    @IgnoredOnParcel
    @Ignore
    @SerializedName("encounterProviders")
    var encounterProviders: List<HashMap<String, String>>? = null

    @IgnoredOnParcel
    @Ignore
    @SerializedName("encounterDatetime")
    var encounterDatetime: String? = encounterTime

    @IgnoredOnParcel
    @Ignore
    @SerializedName("encounterType")
    var encounterType: String? = encounterTypeUuid

    @IgnoredOnParcel
    @Ignore
    @SerializedName("visit")
    var visitId: String? = visitUuid

    override fun toString(): String = Gson().toJson(this)
}
