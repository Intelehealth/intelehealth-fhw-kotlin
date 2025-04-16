package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_patient")
data class Patient(
//    @PrimaryKey
//    @SerializedName("uuid") override var uuid: String = "",
    @ColumnInfo("openmrs_id") @SerializedName("openmrs_id") val openMrsId: String? = null,
    @ColumnInfo("first_name") @SerializedName("firstname") val firstName: String? = null,
    @ColumnInfo("middle_name") @SerializedName("middlename") val middleName: String? = null,
    @ColumnInfo("last_name") @SerializedName("lastname") val lastName: String? = null,
    @ColumnInfo("date_of_birth") @SerializedName("dateofbirth") val dateOfBirth: String? = null,
    @ColumnInfo("phone_number") @SerializedName("phone_number") val phoneNumber: String? = null,
//    @SerializedName("address2") val address2: String? = null,
//    @SerializedName("address1") val address1: String? = null,
//    @SerializedName("address3") val address3: String? = null,
//    @SerializedName("address4") val address4: String? = null,
//    @SerializedName("address5") val address5: String? = null,
//    @SerializedName("address6") val address6: String? = null,
//    @ColumnInfo("city_village") @SerializedName("cityvillage") val cityVillage: String? = null,
//    @ColumnInfo("district") @SerializedName("countyDistrict") val district: String? = null,
//    @ColumnInfo("state") @SerializedName("stateprovince") val state: String? = null,
//    @ColumnInfo("postal_code") @SerializedName("postal_code") val postalCode: String? = null,
//    @SerializedName("country") val country: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("sdw") val sdw: String? = null,
    @SerializedName("occupation") val occupation: String? = null,
    @ColumnInfo("creatoruuid") @SerializedName("creatoruuid") val creatorUuid: String? = null,
    @ColumnInfo("education_status") @SerializedName("education_status") val educationStatus: String? = null,
    @ColumnInfo("economic_status") @SerializedName("economic_status") val economicStatus: String? = null,
    @ColumnInfo("patient_photo") @SerializedName("patient_photo") val patientPhoto: String? = null,
    @SerializedName("caste") val caste: String? = null,
    @SerializedName("dead") val dead: String? = null,
    @ColumnInfo("updated_at") @SerializedName("modified_date") override var updatedAt: String? = null,
    @ColumnInfo("created_at") @SerializedName("dateCreated") override var createdAt: String? = null,
    @ColumnInfo("abha_number") @SerializedName("abha_number") val abhaNumber: String? = null,
    @ColumnInfo("abha_address") @SerializedName("abha_address") val abhaAddress: String? = null,
    @SerializedName("syncd")
    override var synced: Boolean = false
) : PersonAddress(), Parcelable

//data class PatientProfile(
//    @SerializedName("person")
//    @Expose
//    val person: String,
//
//    @SerializedName("base64EncodedImage")
//    @Expose
//    val base64EncodedImage: String
//)
