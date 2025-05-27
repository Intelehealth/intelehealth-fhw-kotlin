package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

/**
 * Created by Vaghela Mithun R. on 13-05-2025 - 19:19.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Parcelize
@Entity
data class PatientAttrWithName(
    var name: String? = null,
) : PatientAttribute(), Parcelable {

    override fun toString(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun mapToPersonalPatientAttrs(
            attrs: List<PatientAttrWithName>,
            otherInfo: PatientOtherInfo
        ): List<PatientAttrWithName> = attrs.map {
            it.synced = false
            when (it.name) {
                PatientAttributeTypeMaster.EMERGENCY_CONTACT_NAME -> it.apply {
                    value = otherInfo.emergencyContactName
                }

                PatientAttributeTypeMaster.EMERGENCY_CONTACT_NUMBER -> it.apply {
                    value = otherInfo.emergencyContactNumber
                }

                PatientAttributeTypeMaster.EMERGENCY_CONTACT_TYPE -> it.apply {
                    value = otherInfo.emergencyContactType
                }

                PatientAttributeTypeMaster.REQUEST_ID -> it.apply {
                    value = otherInfo.requestId
                }

                PatientAttributeTypeMaster.TELEPHONE -> it.apply { value = otherInfo.telephone }
            }
            return@map it
        }


        fun filterNewlyAddedAttrInEditMode(
            attrs: List<PatientAttrWithName>, otherInfo: PatientOtherInfo
        ): PatientOtherInfo = attrs.map { removeExistedAttrs(it, otherInfo) }[0]

        private fun removeExistedAttrs(
            attr: PatientAttrWithName,
            otherInfo: PatientOtherInfo
        ): PatientOtherInfo {
            when (attr.name) {
                PatientAttributeTypeMaster.EMERGENCY_CONTACT_NAME -> otherInfo.emergencyContactName = null
                PatientAttributeTypeMaster.EMERGENCY_CONTACT_NUMBER -> otherInfo.emergencyContactNumber = null
                PatientAttributeTypeMaster.EMERGENCY_CONTACT_TYPE -> otherInfo.emergencyContactType = null
                PatientAttributeTypeMaster.REQUEST_ID -> otherInfo.requestId = null
                PatientAttributeTypeMaster.TELEPHONE -> otherInfo.telephone = null
                PatientAttributeTypeMaster.NATIONAL_ID -> otherInfo.nationalId = null
                PatientAttributeTypeMaster.OCCUPATION -> otherInfo.occupation = null
                PatientAttributeTypeMaster.EDUCATION -> otherInfo.education = null
                PatientAttributeTypeMaster.ECONOMIC_STATUS -> otherInfo.economicStatus = null
                PatientAttributeTypeMaster.TMH_CASE_NUMBER -> otherInfo.tmhCaseNumber = null
                PatientAttributeTypeMaster.RELATIVE_PHONE_NUMBER -> otherInfo.relativePhoneNumber = null
                PatientAttributeTypeMaster.DISCIPLINE -> otherInfo.discipline = null
                PatientAttributeTypeMaster.INN -> otherInfo.inn = null
                PatientAttributeTypeMaster.CODE_OF_HEALTH_FACILITY -> otherInfo.codeOfHealthFacility = null
                PatientAttributeTypeMaster.HEALTH_FACILITY_NAME -> otherInfo.healthFacilityName = null
                PatientAttributeTypeMaster.CODE_OF_DEPARTMENT -> otherInfo.codeOfDepartment = null
                PatientAttributeTypeMaster.DEPARTMENT -> otherInfo.department = null
                PatientAttributeTypeMaster.CAST -> otherInfo.cast = null
            }

            return otherInfo
        }

        fun mapToOtherPatientAttrs(
            attrs: List<PatientAttrWithName>,
            otherInfo: PatientOtherInfo
        ): List<PatientAttrWithName> = attrs.map {
            it.synced = false
            when (it.name) {
                PatientAttributeTypeMaster.NATIONAL_ID -> it.apply {
                    value = otherInfo.nationalId
                }

                PatientAttributeTypeMaster.OCCUPATION -> it.apply {
                    value = otherInfo.occupation
                }

                PatientAttributeTypeMaster.EDUCATION -> it.apply {
                    value = otherInfo.education
                }

                PatientAttributeTypeMaster.ECONOMIC_STATUS -> it.apply {
                    value = otherInfo.economicStatus
                }

                PatientAttributeTypeMaster.TMH_CASE_NUMBER -> it.apply {
                    value = otherInfo.tmhCaseNumber
                }

                PatientAttributeTypeMaster.RELATIVE_PHONE_NUMBER -> it.apply {
                    value = otherInfo.relativePhoneNumber
                }

                PatientAttributeTypeMaster.DISCIPLINE -> it.apply {
                    value = otherInfo.discipline
                }

                PatientAttributeTypeMaster.INN -> it.apply {
                    value = otherInfo.inn
                }

                PatientAttributeTypeMaster.CODE_OF_HEALTH_FACILITY -> it.apply {
                    value = otherInfo.codeOfHealthFacility
                }

                PatientAttributeTypeMaster.HEALTH_FACILITY_NAME -> it.apply {
                    value = otherInfo.healthFacilityName
                }

                PatientAttributeTypeMaster.CODE_OF_DEPARTMENT -> it.apply {
                    value = otherInfo.codeOfDepartment
                }

                PatientAttributeTypeMaster.DEPARTMENT -> it.apply {
                    value = otherInfo.department
                }

                PatientAttributeTypeMaster.CAST -> it.apply {
                    value = otherInfo.cast
                }
            }
            return@map it
        }
    }
}
