package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_patient")
data class Patient(
    @ColumnInfo("openmrs_id") @SerializedName("openmrs_id") val openMrsId: String? = null,
    @ColumnInfo("first_name") @SerializedName("firstname") val firstName: String? = null,
    @ColumnInfo("middle_name") @SerializedName("middlename") val middleName: String? = null,
    @ColumnInfo("last_name") @SerializedName("lastname") val lastName: String? = null,
    @ColumnInfo("date_of_birth") @SerializedName("dateofbirth") val dateOfBirth: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @ColumnInfo("creatoruuid") @SerializedName("creatoruuid") val creatorUuid: String? = null,
    @ColumnInfo("updated_at") @SerializedName("modified_date") override var updatedAt: String? = null,
    @ColumnInfo("created_at") @SerializedName("dateCreated") override var createdAt: String? = null,
    @ColumnInfo("abha_number") @SerializedName("abha_number") val abhaNumber: String? = null,
    @ColumnInfo("abha_address") @SerializedName("abha_address") val abhaAddress: String? = null,
    @ColumnInfo("profile_version") @SerializedName("profile_version") val profileVersion: Long? = null,
    @ColumnInfo("guardian_name") @SerializedName("guardianName") var guardianName: String? = null,
    @ColumnInfo("guardian_type") @SerializedName("guardianType") val guardianType: String? = null,
    @SerializedName("syncd")
    override var synced: Boolean = false
) : PersonAddress(), Parcelable
