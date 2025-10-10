package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_patient")
open class Patient(
    @ColumnInfo("openmrs_id") @SerializedName("openmrs_id") var openMrsId: String? = null,
    @ColumnInfo("first_name") @SerializedName("firstname") var firstName: String? = null,
    @ColumnInfo("middle_name") @SerializedName("middlename") var middleName: String? = null,
    @ColumnInfo("last_name") @SerializedName("lastname") var lastName: String? = null,
    @ColumnInfo("date_of_birth") @SerializedName("dateofbirth") var dateOfBirth: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @ColumnInfo("creatoruuid") @SerializedName("creatoruuid") var creatorUuid: String? = null,
    @ColumnInfo("updated_at") @SerializedName("modified_date") override var updatedAt: String? = null,
    @ColumnInfo("created_at") @SerializedName("dateCreated") override var createdAt: String? = null,
    @ColumnInfo("abha_number") @SerializedName("abha_number") var abhaNumber: String? = null,
    @ColumnInfo("abha_address") @SerializedName("abha_address") var abhaAddress: String? = null,
    @ColumnInfo("profile_version") @SerializedName("profile_version") var profileVersion: Long? = null,
    @ColumnInfo("guardian_name") @SerializedName("guardianName") var guardianName: String? = null,
    @ColumnInfo("guardian_type") @SerializedName("guardianType") var guardianType: String? = null,
    @SerializedName("syncd")
    override var synced: Boolean = false
) : BaseEntity(), Parcelable {

    override fun toString(): String {
        return Gson().toJson(this)
    }

    fun fullName(): String {
        return middleName?.let {
            return@let "$firstName $middleName $lastName"
        } ?: run {
            return@run "$firstName $lastName"
        }
    }

    companion object {
        const val PERSONAL_INFO_FIELDS = "uuid, openmrs_id, first_name, last_name, middle_name, " +
                "date_of_birth, gender, guardian_name, guardian_type, profile_version, abha_address, " +
                "abha_number, creatoruuid, created_at, updated_at, voided, synced"
    }
}
