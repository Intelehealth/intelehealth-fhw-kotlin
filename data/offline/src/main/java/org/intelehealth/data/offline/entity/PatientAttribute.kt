package org.intelehealth.data.offline.entity

import android.icu.text.UnicodeSet.CASE
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_patient_attribute")
open class PatientAttribute(
    @SerializedName("value") var value: String? = null,
    @ColumnInfo("person_attribute_type_uuid") @SerializedName("person_attribute_type_uuid")
    var personAttributeTypeUuid: String? = null,
    @ColumnInfo("patient_uuid") @SerializedName("patientuuid") var patientUuid: String? = null,
    @ColumnInfo("updated_at") @SerializedName("modified_date") override var updatedAt: String? = null,
) : BaseEntity(), Parcelable {

    companion object {
        const val PERSONAL_QUERY_ATTRS =
            " MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.EMERGENCY_CONTACT_NAME}' THEN PA.value END) AS emergencyContactName, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.EMERGENCY_CONTACT_NUMBER}' THEN PA.value END) AS emergencyContactNumber, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.EMERGENCY_CONTACT_TYPE}' THEN PA.value END) AS emergencyContactType, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.REQUEST_ID}' THEN PA.value END) AS requestId," +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.TELEPHONE}' THEN PA.value END) AS telephone "

        const val OTHER_QUERY_ATTRS =
            "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.NATIONAL_ID}' THEN PA.value END) as nationalId, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.OCCUPATION}' THEN PA.value END) as occupation, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.NATIONAL_ID}' THEN PA.value END) as socialCategory, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.EDUCATION}' THEN PA.value END) as education, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.TMH_CASE_NUMBER}' THEN PA.value END) AS tmhCaseNumber, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.RELATIVE_PHONE_NUMBER}' THEN PA.value END) AS relativePhoneNumber, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.DISCIPLINE}' THEN PA.value END) AS discipline, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.DEPARTMENT}' THEN PA.value END) AS department, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.INN}' THEN PA.value END) AS inn, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.HEALTH_FACILITY_NAME}' THEN PA.value END) AS healthFacilityName, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.CODE_OF_DEPARTMENT}' THEN PA.value END) AS codeOfDepartment, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.ECONOMIC_STATUS}' THEN PA.value END) AS economicStatus, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.SWD}' THEN PA.value END) AS swd, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.PROFILE_IMG_TIMESTAMP}' THEN PA.value END) AS profileImgTimestamp, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.CAST}' THEN PA.value END) AS `cast`, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.CREATED_DATE}' THEN PA.value END) AS createdDate, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.PROVINCES}' THEN PA.value END) AS provinces, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.CITIES}' THEN PA.value END) AS cities, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.REGISTRATION_ADDRESS_OF_HF}' THEN PA.value END) AS registrationAddressOfHf, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.CODE_OF_HEALTH_FACILITY}' THEN PA.value END) AS codeOfHealthFacility, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.HOUSEHOLD_UUID_LINKING}' THEN PA.value END) AS householdUuidLinking, " +
                    "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.BLOCK}' THEN PA.value END) AS block "

        const val PATIENT_ALL_ATTRS = "$PERSONAL_QUERY_ATTRS, $OTHER_QUERY_ATTRS"
    }
}
