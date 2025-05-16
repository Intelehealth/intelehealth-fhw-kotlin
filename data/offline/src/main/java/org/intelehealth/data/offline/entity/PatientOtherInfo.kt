package org.intelehealth.data.offline.entity

import androidx.room.Entity
import androidx.room.Ignore
import com.google.gson.Gson

/**
 * Created by Vaghela Mithun R. on 16-04-2025 - 19:44.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Entity
data class PatientOtherInfo(
    var patientId: String? = null,
    var nationalId: String? = null,
    var socialCategory: String? = null,
    var education: String? = null,
    var tmhCaseNumber: String? = null,
    var requestId: String? = null,
    var relativePhoneNumber: String? = null,
    var discipline: String? = null,
    var department: String? = null,
    var inn: String? = null,
    var healthFacilityName: String? = null,
    var codeOfDepartment: String? = null,
    var emergencyContactName: String? = null,
    var emergencyContactNumber: String? = null,
    var emergencyContactType: String? = null,
    var telephone: String? = null,
    var economicStatus: String? = null,
    var providerId: String? = null,
    var occupation: String? = null,
    var swd: String? = null,
    var profileImgTimestamp: String? = null,
    var cast: String? = null,
    var createdDate: String? = null,
    var provinces: String? = null,
    var cities: String? = null,
    var registrationAddressOfHf: String? = null,
    var codeOfHealthFacility: String? = null,
    var householdUuidLinking: String? = null,
    var block: String? = null
) {
    @Ignore
    var patientMasterAttrs: List<PatientAttributeTypeMaster> = emptyList()

    override fun toString(): String {
        return Gson().toJson(this)
    }

    fun getNotNullableAttrsSize(): Int = listOfNotNull(
        nationalId,
        socialCategory,
        education,
        tmhCaseNumber,
        requestId,
        relativePhoneNumber,
        discipline,
        department,
        inn,
        healthFacilityName,
        codeOfDepartment,
        emergencyContactName,
        emergencyContactNumber,
        emergencyContactType,
        telephone,
        economicStatus,
        occupation,
        swd,
        profileImgTimestamp,
        cast,
        createdDate,
        provinces,
        cities,
        registrationAddressOfHf,
        codeOfHealthFacility,
        householdUuidLinking
    ).size
}
