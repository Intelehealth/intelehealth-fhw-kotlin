package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.intelehealth.data.offline.entity.PatientAttrWithName
import org.intelehealth.data.offline.entity.PatientAttribute
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
        "SELECT patient_uuid as patientId, ${PatientAttribute.OTHER_QUERY_ATTRS} "
                + "FROM tbl_patient_attribute PA "
                + "LEFT JOIN tbl_patient_attribute_master PAM ON PAM.uuid = PA.person_attribute_type_uuid "
                + "WHERE patient_uuid = :patientId GROUP BY patientId"
    )
    fun getPatientOtherAttrs(patientId: String): LiveData<PatientOtherInfo>

    @Query(
        "SELECT PA.uuid, PA.patient_uuid, PAM.name, PA.value, PA.person_attribute_type_uuid, PA.synced, PA.voided "
                + "FROM tbl_patient_attribute PA "
                + "LEFT JOIN tbl_patient_attribute_master PAM ON PAM.uuid = PA.person_attribute_type_uuid "
                + "WHERE PA.patient_uuid = :patientId AND PAM.name IN (:names)"
    )
    suspend fun getPatientAttributesByNames(patientId: String, names: List<String>): List<PatientAttrWithName>

    @Query(
        "SELECT patient_uuid as patientId, ${PatientAttribute.PERSONAL_QUERY_ATTRS} "
                + "FROM tbl_patient_attribute PA "
                + "LEFT JOIN tbl_patient_attribute_master PAM ON PAM.uuid = PA.person_attribute_type_uuid "
                + "WHERE patient_uuid = :patientId GROUP BY patientId"
    )
    fun getPatientPersonalAttrsLiveData(patientId: String): LiveData<PatientOtherInfo>

    @Query(
        "SELECT patient_uuid as patientId, ${PatientAttribute.PATIENT_ALL_ATTRS} "
                + "FROM tbl_patient_attribute PA "
                + "LEFT JOIN tbl_patient_attribute_master PAM ON PAM.uuid = PA.person_attribute_type_uuid "
                + "WHERE patient_uuid = :patientId GROUP BY patientId"
    )
    suspend fun getAllPatientAttrsData(patientId: String): PatientOtherInfo
}
