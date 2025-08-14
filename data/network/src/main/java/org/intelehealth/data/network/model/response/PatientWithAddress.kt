package org.intelehealth.data.network.model.response

import com.google.gson.annotations.SerializedName
import org.intelehealth.data.offline.entity.Patient
import org.intelehealth.data.offline.entity.PersonAddress

/**
 * Created by Vaghela Mithun R. on 02-07-2025 - 18:17.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class PatientWithAddress(
    @SerializedName("openmrs_id") val openMrsId: String? = null,
    @SerializedName("firstname") var firstName: String? = null,
    @SerializedName("middlename") var middleName: String? = null,
    @SerializedName("lastname") var lastName: String? = null,
    @SerializedName("dateofbirth") var dateOfBirth: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("creatoruuid") var creatorUuid: String? = null,
    @SerializedName("modified_date") override var updatedAt: String? = null,
    @SerializedName("dateCreated") override var createdAt: String? = null,
    @SerializedName("abha_number") val abhaNumber: String? = null,
    @SerializedName("abha_address") val abhaAddress: String? = null,
    @SerializedName("guardianName") var guardianName: String? = null,
    @SerializedName("guardianType") var guardianType: String? = null,
    @SerializedName("syncd")
    override var synced: Boolean = false
) : PersonAddress() {
    fun toAddress(): PersonAddress {
        return PersonAddress(
            address1 = this.address1,
            address2 = this.address2,
            address3 = this.address3,
            address4 = this.address4,
            address5 = this.address5,
            address6 = this.address6,
            cityVillage = this.cityVillage,
            district = this.district,
            state = this.state,
            postalCode = this.postalCode,
            country = this.country,
            addressOfHf = this.addressOfHf,
        ).apply {
            this.uuid = this@PatientWithAddress.uuid
            this.synced = true
        }
    }

    fun toPatient(): Patient {
        return Patient(
            openMrsId = this.openMrsId,
            firstName = this.firstName,
            middleName = this.middleName,
            lastName = this.lastName,
            dateOfBirth = this.dateOfBirth,
            gender = this.gender,
            creatorUuid = this.creatorUuid,
            updatedAt = this.updatedAt,
            createdAt = this.createdAt,
            abhaNumber = this.abhaNumber,
            abhaAddress = this.abhaAddress,
            guardianName = this.guardianName,
            guardianType = this.guardianType,
            synced = true
        ).apply { uuid = this@PatientWithAddress.uuid }
    }
}
