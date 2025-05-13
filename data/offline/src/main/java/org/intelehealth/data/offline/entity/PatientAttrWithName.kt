package org.intelehealth.data.offline.entity

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

/**
 * Created by Vaghela Mithun R. on 13-05-2025 - 19:19.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Parcelize
@Entity
class PatientAttrWithName(
    var name: String? = null,
) : PatientAttribute(), Parcelable {
    companion object {
        fun mapToPersonalPatientAttrs(
            attrs: List<PatientAttrWithName>,
            otherInfo: PatientOtherInfo
        ): List<PatientAttribute> = attrs.map {
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
    }
}
