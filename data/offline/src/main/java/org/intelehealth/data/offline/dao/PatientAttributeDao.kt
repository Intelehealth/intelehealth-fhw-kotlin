package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.intelehealth.data.offline.entity.PatientAttrWithName
import org.intelehealth.data.offline.entity.PatientAttribute
import org.intelehealth.data.offline.entity.PatientAttributeTypeMaster
import org.intelehealth.data.offline.entity.PatientOtherInfo

@Dao
interface PatientAttributeDao : CoreDao<PatientAttribute> {

    @Query("SELECT * FROM tbl_patient_attribute WHERE patient_uuid = :patientUuid")
    fun getAttributesByPatientUuid(patientUuid: String): LiveData<List<PatientAttribute>>

    @Query("SELECT * FROM tbl_patient_attribute WHERE patient_uuid = :patientUuid")
    suspend fun getPatientAttributesByPatientUuid(patientUuid: String): List<PatientAttribute>

    @Query("SELECT * FROM tbl_patient_attribute WHERE patient_uuid IN ( :patientIds ) AND voided = 0 AND synced = 0")
    suspend fun getPatientAttributes(patientIds: List<String>): List<PatientAttribute>

    @Query("SELECT * FROM tbl_patient_attribute WHERE patient_uuid = :patientUuid AND person_attribute_type_uuid = :personAttributeTypeUuid")
    fun getAttributesByPatientUuidAndPersonAttributeTypeUuid(
        patientUuid: String, personAttributeTypeUuid: String
    ): LiveData<List<PatientAttribute>>

    @Query("SELECT DISTINCT patient_uuid FROM tbl_patient_attribute WHERE value = :value")
    fun getPatientsUuidsByValue(value: String): LiveData<List<String>>

    @Query(
        "SELECT COUNT(PA.patient_uuid) as created FROM tbl_patient_attribute PA "
                + "LEFT JOIN tbl_patient_attribute_master PAM ON PAM.uuid = PA.person_attribute_type_uuid "
                + "LEFT JOIN tbl_user U ON U.provider_uuid = PA.value "
                + "WHERE U.uuid = :userId AND PAM.name = :patientAttrName"
    )
    suspend fun getCreatedPatientCountByUser(userId: String, patientAttrName: String): Int

    @Query(
        "SELECT patient_uuid as patientId, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.NATIONAL_ID}' THEN PA.value END) as nationalId, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.OCCUPATION}' THEN PA.value END) as occupation, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.NATIONAL_ID}' THEN PA.value END) as socialCategory, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.EDUCATION}' THEN PA.value END) as education, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.TMH_CASE_NUMBER}' THEN PA.value END) AS tmhCaseNumber, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.REQUEST_ID}' THEN PA.value END) AS requestId, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.RELATIVE_PHONE_NUMBER}' THEN PA.value END) AS relativePhoneNumber, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.DISCIPLINE}' THEN PA.value END) AS discipline, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.DEPARTMENT}' THEN PA.value END) AS department, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.INN}' THEN PA.value END) AS inn, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.HEALTH_FACILITY_NAME}' THEN PA.value END) AS healthFacilityName, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.CODE_OF_DEPARTMENT}' THEN PA.value END) AS codeOfDepartment, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.EMERGENCY_CONTACT_NAME}' THEN PA.value END) AS emergencyContactName, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.EMERGENCY_CONTACT_NUMBER}' THEN PA.value END) AS emergencyContactNumber, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.EMERGENCY_CONTACT_TYPE}' THEN PA.value END) AS emergencyContactType, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.TELEPHONE}' THEN PA.value END) AS telephone, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.ECONOMIC_STATUS}' THEN PA.value END) AS economicStatus, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.SWD}' THEN PA.value END) AS swd, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.PROFILE_IMG_TIMESTAMP}' THEN PA.value END) AS profileImgTimestamp, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.CAST}' THEN PA.value END) AS `cast`, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.CREATED_DATE}' THEN PA.value END) AS createdDate, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.PROVINCES}' THEN PA.value END) AS provinces, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.CITIES}' THEN PA.value END) AS cities, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.REGISTRATION_ADDRESS_OF_HF}' THEN PA.value END) AS registrationAddressOfHf, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.CODE_OF_HEALTH_FACILITY}' THEN PA.value END) AS codeOfHealthFacility, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.HOUSEHOLD_UUID_LINKING}' THEN PA.value END) AS householdUuidLinking, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.BLOCK}' THEN PA.value END) AS block "
                + "FROM tbl_patient_attribute PA "
                + "LEFT JOIN tbl_patient_attribute_master PAM ON PAM.uuid = PA.person_attribute_type_uuid "
                + "WHERE patient_uuid = :patientId GROUP BY patientId"
    )
    suspend fun getPatientOtherAttrs(patientId: String): PatientOtherInfo

    @Query(
        "SELECT PA.uuid, PA.patient_uuid, PAM.name, PA.value, PA.person_attribute_type_uuid, PA.synced, PA.voided "
                + "FROM tbl_patient_attribute PA "
                + "LEFT JOIN tbl_patient_attribute_master PAM ON PAM.uuid = PA.person_attribute_type_uuid "
                + "WHERE PA.patient_uuid = :patientId AND PAM.name IN (:names)"
    )
    suspend fun getPatientAttributesByNames(patientId: String, names: List<String>): List<PatientAttrWithName>

    @Query(
        "SELECT patient_uuid as patientId, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.EMERGENCY_CONTACT_NAME}' THEN PA.value END) AS emergencyContactName, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.EMERGENCY_CONTACT_NUMBER}' THEN PA.value END) AS emergencyContactNumber, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.EMERGENCY_CONTACT_TYPE}' THEN PA.value END) AS emergencyContactType, "
                + "MAX(CASE WHEN PAM.name = '${PatientAttributeTypeMaster.TELEPHONE}' THEN PA.value END) AS telephone "
                + "FROM tbl_patient_attribute PA "
                + "LEFT JOIN tbl_patient_attribute_master PAM ON PAM.uuid = PA.person_attribute_type_uuid "
                + "WHERE patient_uuid = :patientId GROUP BY patientId"
    )
    fun getPatientPersonalAttrsLiveData(patientId: String): LiveData<PatientOtherInfo>
}
