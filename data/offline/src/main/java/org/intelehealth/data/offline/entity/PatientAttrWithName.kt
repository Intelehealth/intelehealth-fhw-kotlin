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
                PatientAttributeTypeMaster.TELEPHONE -> otherInfo.telephone = null
            }

            return otherInfo
        }
    }
}
