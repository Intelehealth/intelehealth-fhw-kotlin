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
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.NATIONAL_ID}' THEN PA.value END as nationalId, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.OCCUPATION} ' THEN PA.value END as occupation, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.NATIONAL_ID}' THEN PA.value END as socialCategory, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.EDUCATION}' THEN PA.value END as education, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.TMH_CASE_NUMBER}' THEN PA.value END AS tmhCaseNumber, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.REQUEST_ID}' THEN PA.value END AS requestId, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.RELATIVE_PHONE_NUMBER}' THEN PA.value END AS relativePhoneNumber, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.DISCIPLINE}' THEN PA.value END AS discipline, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.DEPARTMENT}' THEN PA.value END AS department, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.INN}' THEN PA.value END AS inn, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.HEALTH_FACILITY_NAME}' THEN PA.value END AS healthFacilityName, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.CODE_OF_DEPARTMENT}' THEN PA.value END AS codeOfDepartment, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.EMERGENCY_CONTACT_NAME}' THEN PA.value END AS emergencyContactName, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.EMERGENCY_CONTACT_NUMBER}' THEN PA.value END AS emergencyContactNumber, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.EMERGENCY_CONTACT_TYPE}' THEN PA.value END AS emergencyContactType, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.TELEPHONE}' THEN PA.value END AS telephone, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.ECONOMIC_STATUS}' THEN PA.value END AS economicStatus, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.SWD}' THEN PA.value END AS swd, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.PROFILE_IMG_TIMESTAMP}' THEN PA.value END AS profileImgTimestamp, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.CAST}' THEN PA.value END AS `cast`, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.CREATED_DATE}' THEN PA.value END AS createdDate, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.PROVINCES}' THEN PA.value END AS provinces, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.CITIES}' THEN PA.value END AS cities, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.REGISTRATION_ADDRESS_OF_HF}' THEN PA.value END AS registrationAddressOfHf, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.CODE_OF_HEALTH_FACILITY}' THEN PA.value END AS codeOfHealthFacility, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.HOUSEHOLD_UUID_LINKING}' THEN PA.value END AS householdUuidLinking, "
                + "CASE WHEN PAM.name = ' ${PatientAttributeTypeMaster.BLOCK}' THEN PA.value END AS block "
                + "FROM tbl_patient_attribute PA "
                + "LEFT JOIN tbl_patient_attribute_master PAM ON PAM.uuid = PA.person_attribute_type_uuid "
                + "WHERE patient_uuid = :patientId GROUP BY patientId"
    )
    suspend fun getPatientOtherDataByUuid(patientId: String): PatientOtherInfo

    @Query(
        "SELECT PA.uuid, PA.patient_uuid, PAM.name, PA.value, PA.person_attribute_type_uuid, PA.synced, PA.voided "
                + "FROM tbl_patient_attribute PA "
                + "LEFT JOIN tbl_patient_attribute_master PAM ON PAM.uuid = PA.person_attribute_type_uuid "
                + "WHERE patient_uuid = :patientId AND name IN (:names) GROUP BY patient_uuid"
    )
    suspend fun getPatientAttributesByNames(patientId: String, names: List<String>): List<PatientAttrWithName>
}
