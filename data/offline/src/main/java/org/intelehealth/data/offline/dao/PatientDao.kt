package org.intelehealth.data.offline.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.intelehealth.data.offline.entity.Patient

/**
 * Created by Vaghela Mithun R. on 02-04-2024 - 10:24.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Dao
interface PatientDao : CoreDao<Patient> {
    @Query("SELECT * FROM tbl_patient WHERE uuid = :uuid")
    fun getPatientByUuid(uuid: String): LiveData<Patient>

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
    fun getPatientByCreatorIdAndDateRange(creatorId: String, fromDate: String, toDate: String): Flow<Int>

    @Query("SELECT * FROM tbl_patient WHERE synced = :synced AND voided = 0")
    suspend fun getAllUnsyncedPatients(synced: Boolean = false): List<Patient>
}
