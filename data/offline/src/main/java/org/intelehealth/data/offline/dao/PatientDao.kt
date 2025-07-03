package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.intelehealth.common.utility.CommonConstants
import org.intelehealth.data.offline.entity.Patient
import org.intelehealth.data.offline.entity.PatientAttributeTypeMaster
import org.intelehealth.data.offline.entity.PersonAddress
import org.intelehealth.data.offline.entity.VisitDetail
import org.intelehealth.data.offline.entity.VisitDetail.Companion.SEARCHABLE

/**
 * Created by Vaghela Mithun R. on 02-04-2024 - 10:24.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Dao
interface PatientDao : CoreDao<Patient> {
    @Query("SELECT ${Patient.PERSONAL_INFO_FIELDS} FROM tbl_patient WHERE uuid = :uuid")
    suspend fun getPatientByUuid(uuid: String): Patient

    @Query("SELECT * FROM tbl_patient WHERE openmrs_id = :openMrsId")
    fun getPatientByOpenMrsId(openMrsId: String): LiveData<Patient>

    @Query("SELECT * FROM tbl_patient WHERE gender = :gender")
    fun getGenderWisePatients(gender: String): LiveData<List<Patient>>

    @Query("SELECT * FROM tbl_patient WHERE creatoruuid = :creatorId")
    fun getPatientByCreatorId(creatorId: String): LiveData<List<Patient>>

    @Query("UPDATE tbl_patient SET openmrs_id = :openMrsId WHERE uuid = :uuid")
    suspend fun updateOpenMrsId(uuid: String, openMrsId: String)

    @Query("UPDATE tbl_patient SET first_name = :firstName WHERE uuid = :uuid")
    suspend fun updateFirstName(uuid: String, firstName: Boolean)

    @Query("UPDATE tbl_patient SET middle_name = :middleName WHERE uuid = :uuid")
    suspend fun updateMiddleName(middleName: String, uuid: String)

    @Query("UPDATE tbl_patient SET last_name = :lastName WHERE uuid = :uuid")
    suspend fun updateLastName(lastName: String, uuid: String)

    @Query("UPDATE tbl_patient SET date_of_birth = :dob WHERE uuid = :uuid")
    suspend fun updateDob(uuid: String, dob: String)

    @Query("UPDATE tbl_patient SET synced = :isSync WHERE uuid = :uuid")
    suspend fun updateSyncStatus(uuid: String, isSync: Boolean)

    @Query("SELECT COUNT(uuid) FROM tbl_patient WHERE creatoruuid = :creatorId")
    fun getPatientCountByCreatorId(creatorId: String): Flow<Int>

    @Query("SELECT COUNT(uuid) FROM tbl_patient WHERE creatoruuid = :creatorId AND date(datetime(created_at)) = :date")
    fun getPatientByCreatorIdAndDate(creatorId: String, date: String): Flow<Int>

    @Query("SELECT COUNT(uuid) FROM tbl_patient WHERE creatoruuid = :creatorId AND date(datetime(created_at)) BETWEEN :fromDate AND :toDate")
    fun getPatientByCreatorIdAndDateRange(
        creatorId: String,
        fromDate: String,
        toDate: String
    ): Flow<Int>

    @Query("SELECT * FROM tbl_patient WHERE synced = :synced AND voided = 0")
    suspend fun getAllUnsyncedPatients(synced: Boolean = false): List<Patient>

    @Query(
        "SELECT P.uuid as  patientId,  P.gender, (P.first_name || ' ' || P.last_name ) full_name, " +
                "${VisitDetail.PATIENT_AGE}, P.openmrs_id,  PA.value as patient_created_at, " +
                "(V.patientuuid = P.uuid) as has_visit, $SEARCHABLE, " +
                "MAX(CASE WHEN E.encounter_type_uuid  = :presConceptId THEN 1 ELSE 0 END) prescription, " +
                "MAX(CASE WHEN E.encounter_type_uuid  = :emergencyConceptId THEN 1 ELSE 0 END) priority, " +
                "MAX(CASE WHEN E.encounter_type_uuid  = :visitCloseConceptId THEN 1 ELSE 0 END) completed " +
                "FROM tbl_patient P " +
                "LEFT OUTER JOIN tbl_visit V ON V.patientuuid = P.uuid " +
                "LEFT OUTER JOIN tbl_encounter E ON E.visituuid = V.uuid " +
                "LEFT OUTER JOIN tbl_patient_attribute PA ON PA.patient_uuid = P.uuid " +
                "LEFT OUTER JOIN tbl_patient_attribute_master PAM ON PAM.uuid = PA.person_attribute_type_uuid " +
                "WHERE PAM.name = '${PatientAttributeTypeMaster.CREATED_DATE}' " +
                "AND searchable LIKE '%' || :searchQuery || '%' " +
                "AND P.synced = 1 AND P.voided = 0 GROUP BY P.uuid ORDER BY P.created_at DESC " +
                "LIMIT ${CommonConstants.LIMIT} OFFSET :offset"
    )
    fun searchPatient(
        presConceptId: String,
        emergencyConceptId: String,
        visitCloseConceptId: String,
        searchQuery: String,
        offset: Int
    ): Flow<List<VisitDetail>>
}
